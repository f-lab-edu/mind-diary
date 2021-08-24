package com.mindDiary.mindDiary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
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

  private static final String EMAIL = "email@google.com";
  private static final String PASSWORD = "password";
  private static final String NICKNAME = "nickname";
  private static final String ACCESS_TOKEN = "access";
  private static final String REFRESH_TOKEN = "refresh";
  private static final int USER_ROLE = 1;
  private static final int USER_ID = 1;
  private static final String EMAIL_TOKEN = "emailToken";

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


  public User getUser() {
    User user = new User();
    user.setId(USER_ID);
    user.setRole(USER_ROLE);
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

  @Test
  @DisplayName("이메일 인증 확인")
  public void checkEmailTokenSuccess() {
    User user = getUser();
    doReturn("1").when(redisStrategy).getValue(EMAIL_TOKEN);
    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doNothing().when(userRepository).updateRole(user);

    assertThat(userService.checkEmailToken(EMAIL_TOKEN, EMAIL)).isTrue();
  }

  @Test
  @DisplayName("이메일 인증 실패")
  public void checkEmailTokenFail() {
    User user = getUser();
    doReturn("110").when(redisStrategy).getValue(EMAIL_TOKEN);
    doReturn(user).when(userRepository).findByEmail(EMAIL);

    assertThat(userService.checkEmailToken(EMAIL_TOKEN, EMAIL)).isFalse();
  }

  @Test
  @DisplayName("로그인 성공")
  public void loginSuccess() {
    User user = getUser();
    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    TokenResponseDTO tokenResponseDTO = getTokenResponseDTO();
    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(true).when(passwordEncoder).matches(any(), any());
    doReturn(ACCESS_TOKEN).when(tokenStrategy).createAccessToken(USER_ID, USER_ROLE, EMAIL);
    doReturn(REFRESH_TOKEN).when(tokenStrategy).createRefreshToken(USER_ID);

    assertThat(userService.login(userLoginRequestDTO)).isEqualTo(tokenResponseDTO);
  }

  @Test
  @DisplayName("로그인 실패 : 유저를 DB에서 찾지 못함")
  public void loginFailByUserNotExists() {
    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    doReturn(null).when(userRepository).findByEmail(EMAIL);

    assertThat(userService.login(userLoginRequestDTO)).isNull();
  }

  @Test
  @DisplayName("로그인 실패 : 패스워드가 일치하지 않음")
  public void loginFail() {
    User user = getUser();
    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doReturn(false).when(passwordEncoder).matches(any(), any());

    assertThat(userService.login(userLoginRequestDTO)).isNull();
  }


  @Test
  @DisplayName("토큰 재발급 성공")
  public void refreshSuccess() {

    User user = getUser();
    TokenResponseDTO tokenResponseDTO = getTokenResponseDTO();

    doReturn(true).when(tokenStrategy).validateToken(REFRESH_TOKEN);

    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(REFRESH_TOKEN);

    doReturn(USER_ID).when(tokenStrategy).getUserId(REFRESH_TOKEN);

    doReturn(user).when(userRepository).findById(USER_ID);

    doReturn(ACCESS_TOKEN).when(tokenStrategy).createAccessToken(USER_ID, USER_ROLE, EMAIL);
    doReturn(REFRESH_TOKEN).when(tokenStrategy).createRefreshToken(USER_ID);

    assertThat(userService.refresh(REFRESH_TOKEN)).isEqualTo(tokenResponseDTO);

  }

  @Test
  @DisplayName("토큰 재발급 실패 : 유효하지 않은 리프레시 토큰이 요청으로 들어옴")
  public void refreshFailByUnvalidToken() {
    doReturn(false).when(tokenStrategy).validateToken(REFRESH_TOKEN);

    assertThat(userService.refresh(REFRESH_TOKEN)).isNull();
  }

  @Test
  @DisplayName("토큰 재발급 실패 : 요청으로 들어온 리프레시 토큰 캐시에 있는 리프레시 토큰과 일치하지 않음")
  public void refreshFailByCache() {
    doReturn(true).when(tokenStrategy).validateToken(REFRESH_TOKEN);

    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(REFRESH_TOKEN);
    doReturn(USER_ID - 1).when(tokenStrategy).getUserId(REFRESH_TOKEN);


    assertThat(userService.refresh(REFRESH_TOKEN)).isNull();

  }


}
