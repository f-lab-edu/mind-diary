package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.exception.businessException.EmailDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.NicknameDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedIdException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedPasswordException;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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
  public void join(UserDTO user) {

    if (isEmailDuplicate(user.getEmail())) {
      throw new EmailDuplicatedException();
    }

    if (isNicknameDuplicate(user.getNickname())) {
      throw new NicknameDuplicatedException();
    }

    user.changeHashedPassword(passwordEncoder);
    UserDTO newUser = UserDTO.createNotPermittedUserWithEmailToken(user);

    userRepository.save(newUser);
    redisStrategy.setValue(newUser.getEmailCheckToken(), String.valueOf(newUser.getId()),
        emailValidityInSeconds);
    emailStrategy.sendMessage(newUser.getEmail(), newUser.getEmailCheckToken());
  }


  private boolean isNicknameDuplicate(String nickname) {
    return userRepository.findByNickname(nickname) != null;
  }

  private boolean isEmailDuplicate(String email) {
    return userRepository.findByEmail(email) != null;
  }

  @Override
  public void checkEmailToken(String token, String email) {
    int id = Integer.parseInt(redisStrategy.getValue(token));
    log.info("redis id : " + id);

    UserDTO user = userRepository.findByEmail(email);
    log.info("user id : " + user.getId());
    if (user.getId() != id) {
      throw new InvalidEmailTokenException();
    }
    redisStrategy.deleteValue(token);
    user.changeRoleUser();
    userRepository.updateRole(user);
  }

  @Override
  public TokenDTO login(UserDTO user) {
    UserDTO findUser = userRepository.findByEmail(user.getEmail());

    if (!passwordEncoder.matches(user.getPassword(), findUser.getPassword())) {
      throw new NotMatchedPasswordException();
    }

    TokenDTO token = TokenDTO.createToken(findUser, tokenStrategy);
    redisStrategy.setValue(token.getRefreshToken(), String.valueOf(findUser.getId()),
        refreshTokenValidityInSeconds);
    return token;
  }



  @Override
  public TokenDTO refresh(String originToken) {

    tokenStrategy.validateToken(originToken);

    String idTakenFromCache = redisStrategy.getValue(originToken);
    int id = Integer.parseInt(idTakenFromCache);

    if (id != tokenStrategy.getUserId(originToken)) {
      throw new NotMatchedIdException();
    }

    UserDTO user = userRepository.findById(id);

    TokenDTO token = TokenDTO.createToken(user, tokenStrategy);
    redisStrategy.deleteValue(originToken);

    redisStrategy.setValue(token.getRefreshToken(), String.valueOf(user.getId()),
        refreshTokenValidityInSeconds);
    return token;
  }

}
