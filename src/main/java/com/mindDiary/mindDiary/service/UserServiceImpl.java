package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.domain.UserRole;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.utils.RedisUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisUtil redisUtil;
  private final EmailService emailService;

  /**
   * 회원 가입 로직
   * 사용자의 패스워드 암호화 - bcrypt( 자동으로 salt 생성해줌 )
   * 사용자가 회원가입을 한다. (회원가입을 한 사용자의 권한은 0 (not_permitted))
   * 캐시에 {이메일 토큰 : user_id } 저장
   * 회원가입 완료와 동시에 사용자에게 인증 메일을 보낸다 ( GET /check-email-token )
   * 사용자는 메일로 받은 링크를 클릭하면 권한을 user로 변경한다.
   * */
  @Override
  public void join(User user) {

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(UserRole.ROLE_NOT_PERMITTED.getRole());

    userRepository.save(user);

    String emailCheckToken = makeEmailCheckToken();
    redisUtil.setValueExpire(emailCheckToken, String.valueOf(user.getId()), 60 * 30L);

    emailService.sendMessage(user.getEmail(),emailCheckToken);
  }

  @Override
  public boolean checkEmailToken(String token, String email) {
    int id = Integer.parseInt(redisUtil.getValueData(token));
    User user = userRepository.findByEmail(email);

    if (user.getId() != id) {
      return false;
    }
    redisUtil.deleteValue(token);
    user.setRole(UserRole.ROLE_USER.getRole());
    userRepository.updateRole(user);
    return true;
  }

  public boolean isEmailDuplicate(String email) {
    return userRepository.findByEmail(email) != null;
  }

  public boolean isNicknameDuplicate(String nickname) {
    return userRepository.findByNickname(nickname) != null;
  }

  private String makeEmailCheckToken() {
    return UUID.randomUUID().toString();
  }
}
