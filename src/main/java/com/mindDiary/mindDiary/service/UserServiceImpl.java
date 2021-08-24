package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisStrategy redisStrategy;
  private final EmailStrategy emailStrategy;
  private final TokenStrategy tokenStrategy;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  @Value("${mailInfo.email-validity-in-seconds}")
  private long emailValidityInSeconds;

  @Override
  public boolean join(UserJoinRequestDTO userJoinRequestDTO) {

    if (isEmailDuplicate(userJoinRequestDTO.getEmail())) {
      return false;
    }

    if (isNicknameDuplicate(userJoinRequestDTO.getNickname())) {
      return false;
    }

    userJoinRequestDTO.changePassword(passwordEncoder);
    User user = User.createUser(userJoinRequestDTO);

    int id = userRepository.save(user);
    if (id == 0) {
      return false;
    }
    redisStrategy.setValue(user.getEmailCheckToken(), String.valueOf(user.getId()),
        emailValidityInSeconds);
    emailStrategy.sendMessage(user.getEmail(), user.getEmailCheckToken());
    return true;
  }

  private boolean isNicknameDuplicate(String nickname) {
    return userRepository.findByNickname(nickname) != null;
  }

  private boolean isEmailDuplicate(String email) {
    return userRepository.findByEmail(email) != null;
  }

  @Override
  public boolean checkEmailToken(String token, String email) {
    int id = Integer.parseInt(redisStrategy.getValue(token));
    User user = userRepository.findByEmail(email);

    if (user.getId() != id) {
      return false;
    }
    redisStrategy.deleteValue(token);
    user.changeRoleUser();
    userRepository.updateRole(user);
    return true;
  }

  @Override
  public TokenResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
    User user = userRepository.findByEmail(userLoginRequestDTO.getEmail());
    if (user == null) {
      return null;
    }

    if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
      return null;
    }

    TokenResponseDTO tokenResponseDTO = user.createToken(tokenStrategy);

    redisStrategy.setValue(tokenResponseDTO.getRefreshToken(), String.valueOf(user.getId()),refreshTokenValidityInSeconds);
    return tokenResponseDTO;
  }

  @Override
  public TokenResponseDTO refresh(String originToken) {

    if (!tokenStrategy.validateToken(originToken)) {
      return null;
    }

    String idTakenFromCache = redisStrategy.getValue(originToken);
    int id = Integer.parseInt(idTakenFromCache);

    if (id != tokenStrategy.getUserId(originToken)) {
      return null;
    }


    User user = userRepository.findById(id);
    TokenResponseDTO tokenResponseDTO = user.createToken(tokenStrategy);

    redisStrategy.deleteValue(originToken);

    redisStrategy.setValue(tokenResponseDTO.getRefreshToken(), String.valueOf(user.getId()),
        refreshTokenValidityInSeconds);
    return tokenResponseDTO;

  }


}
