package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.domain.UserRole;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisStrategy redisStrategy;
  private final EmailStrategy emailStrategy;

  @Override
  public void join(UserJoinRequestDTO userJoinRequestDTO) {

    User user = new User();
    user.setEmail(userJoinRequestDTO.getEmail());
    user.setNickname(userJoinRequestDTO.getNickname());
    user.setPassword(passwordEncoder.encode(userJoinRequestDTO.getPassword()));
    user.setRole(UserRole.ROLE_NOT_PERMITTED.getRole());
    user.createEmailCheckToken();

    userRepository.save(user);

    redisStrategy.setValueExpire(user.getEmailCheckToken(), String.valueOf(user.getId()), 60 * 30L);

    emailStrategy.sendMessage(user.getEmail(),user.getEmailCheckToken());
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
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User findByNickname(String email) {
    return userRepository.findByNickname(email);
  }

}
