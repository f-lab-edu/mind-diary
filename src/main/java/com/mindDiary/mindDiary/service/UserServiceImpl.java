package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.domain.UserRole;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
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
  private final JwtStrategy jwtStrategy;

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

    User user = new User();
    user.setEmail(userJoinRequestDTO.getEmail());
    user.setNickname(userJoinRequestDTO.getNickname());
    user.setPassword(passwordEncoder.encode(userJoinRequestDTO.getPassword()));
    user.setRole(UserRole.ROLE_NOT_PERMITTED.getRole());
    user.createEmailCheckToken();

    int cnt = userRepository.save(user);
    if (cnt == 0) {
      return false;
    }
    redisStrategy.setValueExpire(user.getEmailCheckToken(), String.valueOf(user.getId()), emailValidityInSeconds);

    emailStrategy.sendMessage(user.getEmail(),user.getEmailCheckToken());
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
    int id = Integer.parseInt(redisStrategy.getValueData(token));
    User user = userRepository.findByEmail(email);

    if (user.getId() != id) {
      return false;
    }
    redisStrategy.deleteValue(token);
    user.setRole(UserRole.ROLE_USER.getRole());
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
    String accessToken = jwtStrategy.createAccessToken(user.getId(), user.getRole(), user.getEmail());
    String refreshToken = jwtStrategy.createRefreshToken(user.getId(), user.getRole(), user.getEmail());

    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
    tokenResponseDTO.setAccessToken(accessToken);
    tokenResponseDTO.setRefreshToken(refreshToken);

    redisStrategy.setValueExpire(refreshToken, user.getEmail(), refreshTokenValidityInSeconds);
    return tokenResponseDTO;
  }

  @Override
  public TokenResponseDTO refresh(String orginToken) {
    if (!jwtStrategy.validateToken(orginToken)) {
      return null;
    }

    String emailTakenFromCache = redisStrategy.getValueData(orginToken);
    if (!emailTakenFromCache.equals(jwtStrategy.getUserEmail(orginToken))) {
      return null;
    }

    int userId = jwtStrategy.getUserId(orginToken);
    int userRole = jwtStrategy.getUserRole(orginToken);
    String userEmail = jwtStrategy.getUserEmail(orginToken);

    String newAccessToken = jwtStrategy.createAccessToken(userId, userRole, userEmail);
    String newRefreshToken = jwtStrategy.createRefreshToken(userId, userRole, userEmail);

    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
    tokenResponseDTO.setAccessToken(newAccessToken);
    tokenResponseDTO.setRefreshToken(newRefreshToken);

    redisStrategy.deleteValue(orginToken);
    redisStrategy.setValueExpire(newRefreshToken, userEmail, refreshTokenValidityInSeconds);
    return tokenResponseDTO;
  }


}
