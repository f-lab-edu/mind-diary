package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dao.UserDAO;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.EmailDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.NicknameDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedIdException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedPasswordException;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailStrategy emailStrategy;
  private final TokenStrategy tokenStrategy;
  private final UserTransactionService userTransactionService;
  private final UserDAO userDAO;

  @Override
  @Transactional
  public void join(User user) {
    user.createEmailToken();
    user.changeHashedPassword(passwordEncoder);

    isEmailDuplicate(user.getEmail());
    isNicknameDuplicate(user.getNickname());

    userRepository.save(user);

    userDAO.addEmailToken(user.getEmailCheckToken(), user.getId());
    emailStrategy.sendUserJoinMessage(user.getEmailCheckToken(), user.getEmail());

    userTransactionService.removeCacheAfterRollback(user.getEmailCheckToken());
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

    User user = userRepository.findByEmail(email);

    isValidateUserIdInCache(user.getId(), token);

    updateRoleUser(user);
  }


  public void updateRoleUser(User user) {
    user.changeRoleUser();
    userRepository.updateRole(user);
  }

  public void isValidateUserIdInCache(int userId, String token) {
    int id = userDAO.getUserId(token);
    if (userId != id) {
      throw new InvalidEmailTokenException();
    }
  }

  @Override
  public Token login(String email, String password) {
    User findUser = userRepository.findByEmail(email);

    validatePassword(password, findUser.getPassword());

    Token token = createTokenAndInputCache(findUser);

    return token;
  }

  public Token createTokenAndInputCache(User user) {
    Token token = Token.create(user, tokenStrategy);
    userDAO.addRefreshToken(token.getRefreshToken(), user.getId());
    return token;
  }


  public void validatePassword(String originPassword, String newPassword) {
    if (!passwordEncoder.matches(originPassword, newPassword)) {
      throw new NotMatchedPasswordException();
    }
  }

  @Override
  public Token refresh(String originToken) {

    int id = validateOriginToken(originToken);

    User user = userRepository.findById(id);

    Token token = createTokenAndInputCache(user);

    return token;
  }

  public int validateOriginToken(String originToken) {
    tokenStrategy.validateToken(originToken);
    int id = userDAO.getUserId(originToken);
    if (id != tokenStrategy.getUserId(originToken)) {
      throw new NotMatchedIdException();
    }
    return id;
  }

}
