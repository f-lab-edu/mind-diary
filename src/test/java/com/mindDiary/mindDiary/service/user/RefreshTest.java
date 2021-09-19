package com.mindDiary.mindDiary.service.user;

import static com.mindDiary.mindDiary.service.user.JoinTest.EMAIL;
import static com.mindDiary.mindDiary.service.user.JoinTest.PASSWORD;
import static com.mindDiary.mindDiary.service.user.JoinTest.TOKEN;
import static com.mindDiary.mindDiary.service.user.JoinTest.USER_ID;
import static com.mindDiary.mindDiary.service.user.JoinTest.createUser;
import static com.mindDiary.mindDiary.service.user.LoginTest.REFRESH_TOKEN;
import static com.mindDiary.mindDiary.service.user.LoginTest.createToken;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.InvalidJwtException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedIdException;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
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


  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  TokenStrategy tokenStrategy;

  @Mock
  RedisStrategy redisStrategy;


  @Test
  @DisplayName("입력한 리프레시 토큰이 유효하지 않아 토큰 재발급에 실패한다")
  public void failByInvalidRefreshToken() {
    String originToken = REFRESH_TOKEN;

    doThrow(new InvalidJwtException()).when(tokenStrategy).validateToken(originToken);

    assertThatThrownBy(() -> {
      userService.refresh(originToken);
    }).isInstanceOf(InvalidJwtException.class);

  }

  @Test
  @DisplayName("캐시에 있는 토큰과 입력한 토큰이 일치하지 않아 토큰 재발급에 실패한다")
  public void failByNotMatchedToken() {
    String originToken = REFRESH_TOKEN;

    doReturn(true).when(tokenStrategy).validateToken(originToken);
    doReturn(String.valueOf(USER_ID - 1)).when(redisStrategy).getValue(originToken);
    doReturn(USER_ID).when(tokenStrategy).getUserId(originToken);

    assertThatThrownBy(() -> {
      userService.refresh(originToken);
    }).isInstanceOf(NotMatchedIdException.class);

  }

  @Test
  @DisplayName("토큰 재발급에 성공한다")
  public void success() {
    String originToken = REFRESH_TOKEN;
    User user = createUser();
    Token token = createToken();

    doReturn(true).when(tokenStrategy).validateToken(originToken);
    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(originToken);
    doReturn(USER_ID).when(tokenStrategy).getUserId(originToken);

    doReturn(user).when(userRepository).findById(USER_ID);
    doNothing().when(redisStrategy).deleteValue(originToken);
    doReturn(token.getAccessToken()).when(tokenStrategy)
        .createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    doReturn(token.getRefreshToken()).when(tokenStrategy).createRefreshToken(user.getId());
    doNothing().when(redisStrategy).addRefreshToken(token.getRefreshToken(), user.getId());

    userService.refresh(originToken);

    verify(tokenStrategy, times(1)).validateToken(originToken);
    verify(redisStrategy, times(1)).getValue(originToken);
    verify(tokenStrategy, times(1)).getUserId(originToken);
    verify(userRepository, times(1)).findById(USER_ID);
    verify(redisStrategy, times(1)).deleteValue(originToken);
    verify(tokenStrategy, times(1)).createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    verify(tokenStrategy, times(1)).createRefreshToken(user.getId());
    verify(redisStrategy, times(1)).addRefreshToken(token.getRefreshToken(), user.getId());
  }
}
