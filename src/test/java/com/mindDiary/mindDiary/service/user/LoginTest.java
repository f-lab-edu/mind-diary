package com.mindDiary.mindDiary.service.user;

import static com.mindDiary.mindDiary.service.user.JoinTest.EMAIL;
import static com.mindDiary.mindDiary.service.user.JoinTest.PASSWORD;
import static com.mindDiary.mindDiary.service.user.JoinTest.createUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.InvalidJwtException;
import com.mindDiary.mindDiary.exception.businessException.BusinessException;
import com.mindDiary.mindDiary.exception.businessException.EmailDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedIdException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedPasswordException;
import com.mindDiary.mindDiary.exception.businessException.RedisAddValueException;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.service.UserServiceImpl;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
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
public class LoginTest {

  public static String ACCESS_TOKEN = "accessToken";
  public static String REFRESH_TOKEN = "refreshToken";

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  TokenStrategy tokenStrategy;

  @Mock
  RedisStrategy redisStrategy;

  public static Token createToken() {
    Token token = new Token();
    token.setAccessToken(ACCESS_TOKEN);
    token.setRefreshToken(REFRESH_TOKEN);
    return token;
  }

  @Test
  @DisplayName("입력한 비밀번호와 DB에 있는 비밀번호가 달라 로그인에 실패한다")
  public void failByNotMatchedEmail() {
    User user = createUser();

    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(false).when(passwordEncoder).matches(PASSWORD, user.getPassword());

    assertThatThrownBy(() -> {
      userService.login(EMAIL, PASSWORD);
    }).isInstanceOf(NotMatchedPasswordException.class);

  }

  @Test
  @DisplayName("토큰을 발급하지 못해 로그인에 실패한다")
  public void failByToken() {
    User user = createUser();
    Token token = createToken();

    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(true).when(passwordEncoder).matches(PASSWORD, user.getPassword());
    doThrow(new InvalidJwtException()).when(tokenStrategy).createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());

    assertThatThrownBy(() -> {
      userService.login(EMAIL, PASSWORD);
    }).isInstanceOf(InvalidJwtException.class);
  }

  @Test
  @DisplayName("리프레시 토큰 재발급 시에 검증용으로 사용될 값을 캐시에 넣지 못해 로그인에 실패한다")
  public void failByCache() {
    User user = createUser();
    Token token = createToken();

    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(true).when(passwordEncoder).matches(PASSWORD, user.getPassword());
    doReturn(token.getAccessToken()).when(tokenStrategy).createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    doReturn(token.getRefreshToken()).when(tokenStrategy).createRefreshToken(user.getId());

    doThrow(new RedisAddValueException()).when(redisStrategy)
        .addRefreshToken(token.getRefreshToken(), user.getId());

    assertThatThrownBy(() -> {
      userService.login(EMAIL, PASSWORD);
    }).isInstanceOf(RedisAddValueException.class);
  }


  @Test
  @DisplayName("로그인에 성공한다")
  public void success() {

    User user = createUser();
    Token token = createToken();

    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(true).when(passwordEncoder).matches(PASSWORD, user.getPassword());
    doReturn(token.getAccessToken()).when(tokenStrategy).createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    doReturn(token.getRefreshToken()).when(tokenStrategy).createRefreshToken(user.getId());
    doNothing().when(redisStrategy).addRefreshToken(token.getRefreshToken(), user.getId());

    userService.login(EMAIL, PASSWORD);

    verify(userRepository, times(1)).findByEmail(EMAIL);
    verify(passwordEncoder, times(1)).matches(PASSWORD, user.getPassword());
    verify(tokenStrategy, times(1)).createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    verify(tokenStrategy, times(1)).createRefreshToken(user.getId());
    verify(redisStrategy, times(1)).addRefreshToken(token.getRefreshToken(), user.getId());
  }

}
