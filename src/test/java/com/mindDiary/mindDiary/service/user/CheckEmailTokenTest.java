package com.mindDiary.mindDiary.service.user;

import static com.mindDiary.mindDiary.service.user.JoinTest.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dao.UserDAO;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import com.mindDiary.mindDiary.mapper.UserRepository;
import com.mindDiary.mindDiary.service.UserServiceImpl;
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
  UserDAO userDAO;

  @Test
  @DisplayName("회원가입용 이메일 인증에 성공한다")
  void success() {

    // Arrange
    User user = createUser();
    String email = user.getEmail();
    int userId = user.getId();
    String token = user.getEmailCheckToken();

    doReturn(user)
        .when(userRepository)
        .findByEmail(email);

    doReturn(userId)
        .when(userDAO).getUserId(token);

    doReturn(1)
        .when(userRepository)
        .updateRole(any(User.class));

    // Act
    userService.checkEmailToken(token, email);

    // Assert
    assertThat(user.getRole())
        .isEqualTo(Role.USER);

  }

  @Test
  @DisplayName("이메일 인증 토큰이 캐시에 있는 토큰과 일치하지 않을 경우 이메일 인증에 실패한다")
  void failByInvalidCache() {
    // Arrange
    User user = createUser();
    String email = user.getEmail();

    String token = user.getEmailCheckToken();
    int otherUserId = 8;

    doReturn(user)
        .when(userRepository)
        .findByEmail(email);

    doReturn(otherUserId)
        .when(userDAO).getUserId(token);

    assertThatThrownBy(() -> {
      userService.checkEmailToken(token, email);
    }).isInstanceOf(InvalidEmailTokenException.class);

    verify(userRepository, times(0))
        .updateRole(user);

    assertThat(user.getRole())
        .isEqualTo(Role.NOT_PERMITTED);

  }

}
