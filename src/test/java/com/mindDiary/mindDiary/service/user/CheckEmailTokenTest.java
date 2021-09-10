package com.mindDiary.mindDiary.service.user;

import static com.mindDiary.mindDiary.service.user.JoinTest.EMAIL;
import static com.mindDiary.mindDiary.service.user.JoinTest.TOKEN;
import static com.mindDiary.mindDiary.service.user.JoinTest.USER_ID;
import static com.mindDiary.mindDiary.service.user.JoinTest.createUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
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
public class CheckEmailTokenTest {

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Mock
  RedisStrategy redisStrategy;


  @Test
  @DisplayName("회원가입용 이메일 인증에 성공한다")
  public void success() {
    User user = createUser();
    user.changeRoleUser();

    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(TOKEN);
    doReturn(1).when(userRepository).updateRole(any(User.class));

    userService.checkEmailToken(TOKEN, EMAIL);

    verify(userRepository, times(1)).findByEmail(EMAIL);
    verify(redisStrategy,times(1)).getValue(TOKEN);
    verify(userRepository, times(1)).updateRole(any(User.class));

  }

  @Test
  @DisplayName("이메일 인증 토큰이 캐시에 있는 토큰과 일치하지 않을 경우 이메일 인증에 실패한다")
  public void failByInvalidCache() {
    User user = createUser();
    user.changeRoleUser();
    int notMatchedUserId = 1000;

    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(String.valueOf(notMatchedUserId)).when(redisStrategy).getValue(TOKEN);

    assertThatThrownBy(() -> {
      userService.checkEmailToken(TOKEN, EMAIL);
    }).isInstanceOf(InvalidEmailTokenException.class);

  }

}
