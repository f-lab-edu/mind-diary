package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.exception.InvalidJwtException;
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


  @Override
  public void join(UserDTO user) {

    isEmailDuplicate(user.getEmail());

    isNicknameDuplicate(user.getNickname());

    UserDTO newUser = user.createNotPermittedUserWithEmailToken();
    newUser.changeHashedPassword(passwordEncoder);

    userRepository.save(newUser);
    redisStrategy.addEmailToken(newUser);
    emailStrategy.sendUserJoinMessage(newUser);

  }


  public void isNicknameDuplicate(String nickname) {
    if (userRepository.findByNickname(nickname) != null) {
      throw new NicknameDuplicatedException();
    }
  }

  public void isEmailDuplicate(String email) {
    if (userRepository.findByEmail(email) != null) {
      throw new EmailDuplicatedException();
    }
  }

  @Override
  public void checkEmailToken(String token, String email) {
    int id = Integer.parseInt(redisStrategy.getValue(token));

    UserDTO user = userRepository.findByEmail(email);

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

    validatePassword(user.getPassword(), findUser.getPassword());

    TokenDTO token = TokenDTO.create(findUser, tokenStrategy);
    redisStrategy.addRefreshToken(token, findUser);

    return token;
  }

  private void validatePassword(String originPassword, String newPassword) {
    if (!passwordEncoder.matches(originPassword, newPassword)) {
      throw new NotMatchedPasswordException();
    }
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

    TokenDTO token = TokenDTO.create(user, tokenStrategy);
    redisStrategy.deleteValue(originToken);
    redisStrategy.addRefreshToken(token, user);
    return token;
  }
}
