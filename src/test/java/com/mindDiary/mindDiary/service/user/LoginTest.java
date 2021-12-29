package com.mindDiary.mindDiary.service.user;

import static com.mindDiary.mindDiary.service.user.JoinTest.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dao.UserDAO;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.InvalidJwtException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedPasswordException;
import com.mindDiary.mindDiary.mapper.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
import com.mindDiary.mindDiary.strategy.token.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class LoginTest {

  public static final String ACCESS_TOKEN = "access";
  public static final String REFRESH_TOKEN = "refresh";

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Mock
  UserDAO userDAO;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  TokenGenerator tokenGenerator;

  @Test
  @DisplayName("입력한 비밀번호와 DB에 있는 비밀번호가 달라 로그인에 실패한다")
  void failByNotMatchedEmail() {

    // Arrange
    User user = createUser();
    int userId = user.getId();
    String role = user.getRole().toString();
    String email = user.getEmail();
    String password = user.getPassword();
    String inputPassword = "other";

    doReturn(user)
        .when(userRepository)
        .findByEmail(email);

    doReturn(false)
        .when(passwordEncoder)
        .matches(inputPassword, password);

    // Act, Assert
    assertThatThrownBy(() -> {
      userService.login(email, inputPassword);
    }).isInstanceOf(NotMatchedPasswordException.class);

    verify(userDAO, times(0))
        .addRefreshToken(anyString(), anyInt());
    verify(tokenGenerator, times(0))
        .createAccessToken(userId, role, email);
    verify(tokenGenerator, times(0))
        .createRefreshToken(userId);
  }

  @Test
  @DisplayName("토큰을 발급하지 못해 로그인에 실패한다")
  void failByToken() {

    // Arrange
    User user = createUser();
    int userId = user.getId();
    String email = user.getEmail();
    String password = user.getPassword();

    doReturn(user)
        .when(userRepository)
        .findByEmail(email);

    doReturn(true)
        .when(passwordEncoder)
        .matches(password, user.getPassword());

    doThrow(new InvalidJwtException())
        .when(tokenGenerator)
        .createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());

    assertThatThrownBy(() -> {
      userService.login(email, password);
    }).isInstanceOf(InvalidJwtException.class);

    verify(tokenGenerator, times(0))
        .createRefreshToken(userId);
    verify(userDAO, times(0))
        .addRefreshToken(anyString(), anyInt());


  }

  @Test
  @DisplayName("로그인에 성공한다")
  void success() {

    // Arrange
    User user = createUser();
    int userId = user.getId();
    String email = user.getEmail();
    String password = user.getPassword();
    String role = user.getRole().toString();

    Token token = new Token(ACCESS_TOKEN, REFRESH_TOKEN);
    String accessToken = token.getAccessToken();
    String refreshToken = token.getRefreshToken();

    doReturn(user)
        .when(userRepository)
        .findByEmail(email);

    doReturn(true)
        .when(passwordEncoder)
        .matches(password, user.getPassword());

    doReturn(accessToken)
        .when(tokenGenerator)
        .createAccessToken(userId, role, email);

    doReturn(refreshToken)
        .when(tokenGenerator)
        .createRefreshToken(userId);

    Token result = userService.login(email, password);

    assertThat(result.getAccessToken())
        .isEqualTo(accessToken);

    assertThat(result.getRefreshToken())
        .isEqualTo(refreshToken);

    verify(userDAO, times(1))
        .addRefreshToken(refreshToken, userId);
  }

}
