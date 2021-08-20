package com.mindDiary.mindDiary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import javax.servlet.http.Cookie;
import javax.validation.constraints.Email;
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
public class UserServiceTest {

  private static final String JOIN_URL =  "/auth/join";
  private static final String LOGIN_URL = "/auth/login";
  private static final String CHECK_EMAIL_TOKEN_URL = "/auth/check-email-token";
  private static final String EMAIL = "email@google.com";
  private static final String PASSWORD = "password";
  private static final String NICKNAME = "nickname";
  private static final String ACCESS_TOKEN = "access";
  private static final String REFRESH_TOKEN = "refresh";

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  RedisStrategy redisStrategy;

  @Mock
  TokenStrategy tokenStrategy;

  @Mock
  EmailStrategy emailStrategy;

  public UserJoinRequestDTO getUserJoinRequestDTO() {
    UserJoinRequestDTO userJoinRequestDTO = new UserJoinRequestDTO();
    userJoinRequestDTO.setPassword(PASSWORD);
    userJoinRequestDTO.setEmail(EMAIL);
    userJoinRequestDTO.setNickname(NICKNAME);
    return userJoinRequestDTO;
  }


  public UserLoginRequestDTO getUserLoginRequestDTO() {
    UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO();
    userLoginRequestDTO.setPassword(PASSWORD);
    userLoginRequestDTO.setEmail(EMAIL);
    return userLoginRequestDTO;
  }

  public Cookie getCookie() {
    Cookie cookie = new Cookie("key", "value");
    return cookie;
  }

  public User getUser() {
    User user = new User();
    user.setId(1);
    user.setRole(1);
    user.setEmail(EMAIL);
    user.setPassword(PASSWORD);
    user.setNickname(NICKNAME);
    return user;
  }

  private TokenResponseDTO getTokenResponseDTO() {
    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(ACCESS_TOKEN, REFRESH_TOKEN);
    return tokenResponseDTO;
  }
  
  @Test
  @DisplayName("회원가입 성공")
  public void join() {
    User user = getUser();
    doReturn(null).when(userRepository).findByEmail(EMAIL);
    doReturn(null).when(userRepository).findByNickname(NICKNAME);
    doReturn(1).when(userRepository).save(any(User.class));

    assertThat(userService.join(getUserJoinRequestDTO())).isTrue();
  }

  @Test
  @DisplayName("회원가입 실패 : 이메일 중복")
  public void joinFailByEmailDuplicate() {
    User user = getUser();
    doReturn(user).when(userRepository).findByEmail(EMAIL);

    assertThat(userService.join(getUserJoinRequestDTO())).isFalse();
  }

  @Test
  @DisplayName("회원가입 실패 : 닉네임 중복")
  public void joinFailByNicknameDuplicate() {
    User user = getUser();
    doReturn(null).when(userRepository).findByEmail(EMAIL);
    doReturn(user).when(userRepository).findByNickname(NICKNAME);

    assertThat(userService.join(getUserJoinRequestDTO())).isFalse();
  }
}
