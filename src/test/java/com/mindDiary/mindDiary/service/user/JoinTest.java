package com.mindDiary.mindDiary.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.EmailDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.MailSendFailedException;
import com.mindDiary.mindDiary.exception.businessException.NicknameDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.RedisAddValueException;
import com.mindDiary.mindDiary.mapper.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JoinTest {

  public static final String EMAIL = "meme2367@naver.com";
  public static final String NICKNAME = "규유유";
  public static final String PASSWORD = "password";
  public static int USER_ID = 1;
  public static final String TOKEN = UUID.randomUUID().toString();

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Mock
  RedisStrategy redisStrategy;

  @Mock
  EmailStrategy emailStrategy;

  @Spy
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  public static User createUser() {
    User user = new User();
    user.setId(USER_ID);
    user.setEmail(EMAIL);
    user.setNickname(NICKNAME);
    user.setPassword(PASSWORD);
    user.setRole(Role.NOT_PERMITTED);
    user.setEmailCheckToken(TOKEN);
    return user;
  }

  @Test
  @DisplayName("이미 등록된 이메일인 경우 회원가입에 실패한다")
  public void joinFailByDuplicateEmail() {

    User user = createUser();
    user.changeHashedPassword(passwordEncoder);

    doReturn(user).when(userRepository).findByEmail(user.getEmail());

    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(EmailDuplicatedException.class);

  }

  @Test
  @DisplayName("이미 등록된 닉네임인 경우 회원가입에 실패한다")
  public void joinFailByDuplicateNickname() {
    User user = createUser();
    user.changeHashedPassword(passwordEncoder);
    doReturn(null).when(userRepository).findByEmail(user.getEmail());
    doReturn(user).when(userRepository).findByNickname(user.getNickname());

    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(NicknameDuplicatedException.class);
  }

  @Test
  @DisplayName("회원가입 인증 메일을 보내지 못한 경우 회원가입에 실패한다")
  public void sendEmailFail() {
    User user = createUser();
    user.changeHashedPassword(passwordEncoder);

    doReturn(null).when(userRepository).findByEmail(user.getEmail());
    doReturn(null).when(userRepository).findByNickname(user.getNickname());

    doReturn(1).when(userRepository).save(any(User.class));
    doNothing().when(redisStrategy).addEmailToken(user.getEmailCheckToken(), user.getId());
    doThrow(new MailSendFailedException()).when(emailStrategy)
        .sendUserJoinMessage(user.getEmailCheckToken(), user.getEmail());

    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(MailSendFailedException.class);


  }

  @Test
  @DisplayName("캐시에 인증 메일 확인용 값을 넣지 못한 경우 회원가입에 실패한다")
  public void inputCacheEmailTokenFail() {
    User user = createUser();
    user.changeHashedPassword(passwordEncoder);

    doReturn(null).when(userRepository).findByEmail(user.getEmail());
    doReturn(null).when(userRepository).findByNickname(user.getNickname());

    doReturn(1).when(userRepository).save(any(User.class));
    doThrow(new RedisAddValueException()).when(redisStrategy)
        .addEmailToken(user.getEmailCheckToken(), user.getId());

    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(RedisAddValueException.class);

  }

  @Test
  @DisplayName("회원가입 성공")
  public void success() {
    User user = createUser();
    String originalPassword = user.getPassword();
    user.changeHashedPassword(passwordEncoder);
    String encryptPassword = user.getPassword();


    doReturn(null).when(userRepository).findByEmail(user.getEmail());
    doReturn(null).when(userRepository).findByNickname(user.getNickname());

    doReturn(1).when(userRepository).save(any(User.class));
    doNothing().when(redisStrategy).addEmailToken(user.getEmailCheckToken(), user.getId());
    doNothing().when(emailStrategy).sendUserJoinMessage(user.getEmailCheckToken(), user.getEmail());

    userService.join(user);

    verify(userRepository, times(1)).findByEmail(user.getEmail());
    verify(userRepository, times(1)).findByNickname(user.getNickname());
    verify(userRepository, times(1)).save(any(User.class));
    verify(redisStrategy, times(1)).addEmailToken(user.getEmailCheckToken(), user.getId());
    verify(emailStrategy, times(1)).sendUserJoinMessage(user.getEmailCheckToken(), user.getEmail());

    assertThat(passwordEncoder.matches(originalPassword, encryptPassword)).isTrue();
  }
}
