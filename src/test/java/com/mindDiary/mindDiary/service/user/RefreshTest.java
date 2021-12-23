package com.mindDiary.mindDiary.service.user;

import static com.mindDiary.mindDiary.service.user.JoinTest.createUser;
import static com.mindDiary.mindDiary.service.user.LoginTest.ACCESS_TOKEN;
import static com.mindDiary.mindDiary.service.user.LoginTest.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dao.UserDAO;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.InvalidJwtException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedIdException;
import com.mindDiary.mindDiary.mapper.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RefreshTest {


  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Mock
  UserDAO userDAO;

  @Mock
  TokenStrategy tokenStrategy;

  @Test
  @DisplayName("입력한 리프레시 토큰이 유효하지 않아 토큰 재발급에 실패한다")
  void failByInvalidRefreshToken() {

    // Arrange
    String originRefreshToken = REFRESH_TOKEN;
    User user = createUser();
    int userId = user.getId();
    String role = user.getRole().toString();
    String email = user.getEmail();

    doThrow(new InvalidJwtException())
        .when(tokenStrategy)
        .validateToken(originRefreshToken);

    // Act
    assertThatThrownBy(() -> {
      userService.refresh(originRefreshToken);
    }).isInstanceOf(InvalidJwtException.class);

    // Assert
    verify(userDAO, times(0))
        .getUserId(originRefreshToken);
    verify(tokenStrategy, times(0))
        .createAccessToken(userId, role, email);
    verify(tokenStrategy, times(0))
        .createRefreshToken(userId);
    verify(userDAO, times(0))
        .addRefreshToken(anyString(), anyInt());
  }

  @Test
  @DisplayName("캐시에 있는 토큰의 user id와 "
      + "입력한 토큰의 userId가"
      + " 일치하지 않아 토큰 재발급에 실패한다")
  void failByNotMatchedToken() {

    String originRefreshToken = REFRESH_TOKEN;
    User user = createUser();
    int userId = user.getId();
    String role = user.getRole().toString();
    String email = user.getEmail();
    int otherUserId = 8;

    doReturn(true)
        .when(tokenStrategy)
        .validateToken(originRefreshToken);

    doReturn(userId)
        .when(userDAO)
        .getUserId(originRefreshToken);

    doReturn(otherUserId)
        .when(tokenStrategy)
        .getUserId(originRefreshToken);

    // Act
    assertThatThrownBy(() -> {
      userService.refresh(originRefreshToken);
    }).isInstanceOf(NotMatchedIdException.class);

    verify(tokenStrategy, times(0))
        .createAccessToken(userId, role, email);
    verify(tokenStrategy, times(0))
        .createRefreshToken(userId);
    verify(userDAO, times(0))
        .addRefreshToken(anyString(), anyInt());

  }

  @Test
  @DisplayName("토큰 재발급에 성공한다")
  void success() {
    String refreshToken = REFRESH_TOKEN;
    String accessToken = ACCESS_TOKEN;
    User user = createUser();
    user.changeRoleUser();
    int userId = user.getId();
    String role = user.getRole().toString();
    String email = user.getEmail();

    doReturn(true)
        .when(tokenStrategy)
        .validateToken(refreshToken);

    doReturn(userId)
        .when(userDAO)
        .getUserId(refreshToken);

    doReturn(user)
        .when(userRepository)
        .findById(userId);

    doReturn(userId)
        .when(tokenStrategy)
        .getUserId(refreshToken);

    doReturn(accessToken)
        .when(tokenStrategy)
        .createAccessToken(userId, role, email);

    doReturn(refreshToken)
        .when(tokenStrategy)
        .createRefreshToken(userId);

    doNothing()
        .when(userDAO)
        .addRefreshToken(refreshToken, userId);
    // Act
    Token result = userService.refresh(refreshToken);

    assertThat(result.getAccessToken())
        .isEqualTo(accessToken);
    assertThat(result.getRefreshToken())
        .isEqualTo(refreshToken);
  }
}
