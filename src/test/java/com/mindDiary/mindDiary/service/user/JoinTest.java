package com.mindDiary.mindDiary.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dao.UserDAO;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.EmailDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import com.mindDiary.mindDiary.exception.businessException.InvalidPasswordException;
import com.mindDiary.mindDiary.exception.businessException.NicknameDuplicatedException;
import com.mindDiary.mindDiary.mapper.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
import com.mindDiary.mindDiary.service.UserTransactionServiceImpl;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.randomToken.RandomTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JoinTest {

  public final static int USER_ID = 1;
  public final static String USER_EMAIL = "meme2367@naver.com";
  public final static String USER_NICKNAME = "규유";
  public static final String USER_PASSWORD = "password";
  public static final String USER_EMAIL_CHECK_TOKEN = "emailtokenxxxxx";


  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Spy
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Mock
  UserDAO userDAO;

  @Mock
  EmailStrategy emailStrategy;

  @Mock
  UserTransactionServiceImpl userTransactionService;

  @Mock
  RandomTokenGenerator tokenGenerator;

  public static User createUser() {
    return User.builder()
        .id(USER_ID)
        .email(USER_EMAIL)
        .nickname(USER_NICKNAME)
        .password(USER_PASSWORD)
        .role(Role.NOT_PERMITTED)
        .emailCheckToken(USER_EMAIL_CHECK_TOKEN)
        .build();
  }

  @Test
  @DisplayName("이미 등록된 이메일인 경우 회원가입에 실패한다")
  void joinFailByDuplicateEmail() {
    // Arrange
    User user = createUser();
    String email = user.getEmail();
    String nickname = user.getNickname();
    String emailToken = user.getEmailCheckToken();

    doReturn(emailToken)
        .when(tokenGenerator)
        .create();

    doReturn(user)
        .when(userRepository)
        .findByEmail(email);

    // Act, assert
    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(EmailDuplicatedException.class);

    verify(userRepository, times(0))
        .findByNickname(nickname);

    verify(userRepository, times(0))
        .save(user);
  }

  @Test
  @DisplayName("이미 등록된 닉네임인 경우 회원가입에 실패한다")
  void joinFailByDuplicateNickname() {
    // Arrange
    User user = createUser();
    String email = user.getEmail();
    String nickname = user.getNickname();
    String emailToken = user.getEmailCheckToken();

    doReturn(emailToken)
        .when(tokenGenerator)
        .create();

    doReturn(null)
        .when(userRepository).findByEmail(email);

    doReturn(user)
        .when(userRepository)
        .findByNickname(nickname);

    // Act, Assert
    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(NicknameDuplicatedException.class);

    verify(userRepository, times(0))
        .save(user);
  }

  @Test
  @DisplayName("회원가입 성공")
  void success() {
    // Arrange
    User user = createUser();
    String email = user.getEmail();
    String nickname = user.getNickname();
    String originPasswd = user.getPassword();
    String emailToken = user.getEmailCheckToken();

    doReturn(emailToken)
        .when(tokenGenerator)
        .create();

    doReturn(null)
        .when(userRepository)
        .findByEmail(email);

    doReturn(null)
        .when(userRepository)
        .findByNickname(nickname);

    // Act
    userService.join(user);

    verify(userRepository, times(1))
        .save(argThat(u -> u.getEmail().equals(email) &&
            u.getNickname().equals(nickname)));

    assertThat(
        passwordEncoder
            .matches(originPasswd, user.getPassword()))
        .isTrue();

    assertThat(user.getEmailCheckToken())
        .isEqualTo(emailToken);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("이메일 토큰을 만들지 못하면 회원가입에 실패한다.")
  void joinFail1(String str) {
    // Arrange
    User user = createUser();
    String email = user.getEmail();
    String nickname = user.getNickname();
    String originPasswd = user.getPassword();
    String emailToken = user.getEmailCheckToken();

    doReturn(str)
        .when(tokenGenerator)
        .create();

    // Act
    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(InvalidEmailTokenException.class);


    verify(userRepository, times(0))
        .save(any(User.class));

    assertThat(user.getEmailCheckToken())
        .isNullOrEmpty();

  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("hashed password를 만들지 못하면 회원가입에 실패한다.")
  void joinFail2(String str) {
    // Arrange
    User user = createUser();
    String email = user.getEmail();
    String nickname = user.getNickname();
    String originPasswd = user.getPassword();
    String emailToken = user.getEmailCheckToken();

    doReturn(emailToken)
        .when(tokenGenerator)
        .create();

    doReturn(str)
        .when(passwordEncoder)
        .encode(originPasswd);

    // Act
    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(InvalidPasswordException.class);


    verify(userRepository, times(0))
        .save(any(User.class));
  }
}
