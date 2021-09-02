package com.mindDiary.mindDiary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dto.Role;
import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.exception.businessException.EmailDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import com.mindDiary.mindDiary.exception.businessException.NicknameDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedIdException;
import com.mindDiary.mindDiary.exception.businessException.NotMatchedPasswordException;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private static final String EMAIL = "email@google.com";
  private static final String PASSWORD = "password";
  private static final String NICKNAME = "nickname";
  private static final String ACCESS_TOKEN = "access";
  private static final String REFRESH_TOKEN = "refresh";
  private static final int USER_ID = 1;
  private static final String EMAIL_TOKEN = "emailToken";

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserServiceImpl userService;

  @Spy
  PasswordEncoder passwordEncoder;

  @Mock
  RedisStrategy redisStrategy;

  @Mock
  TokenStrategy tokenStrategy;

  @Mock
  EmailStrategy emailStrategy;


  private TokenDTO getTokenDTO() {
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setAccessToken(ACCESS_TOKEN);
    tokenDTO.setRefreshToken(REFRESH_TOKEN);
    return tokenDTO;
  }

  public UserDTO getUser() {
    UserDTO user = new UserDTO();
    user.setId(USER_ID);
    user.setRole(Role.USER);
    user.setEmail(EMAIL);
    user.setPassword(PASSWORD);
    user.setNickname(NICKNAME);
    user.setEmailCheckToken(EMAIL_TOKEN);
    return user;
  }

  public UserDTO joinUser() {
    UserDTO user = new UserDTO();
    user.setEmail(EMAIL);
    user.setPassword(PASSWORD);
    user.setNickname(NICKNAME);
    return user;
  }

  public UserDTO loginUser() {
    UserDTO user = new UserDTO();
    user.setEmail(EMAIL);
    user.setPassword(PASSWORD);
    return user;
  }


  @Test
  @DisplayName("회원가입 성공")
  public void join() {

    UserDTO user = joinUser();
    passwordEncoder = new BCryptPasswordEncoder();

    doReturn(null).when(userRepository).findByEmail(user.getEmail());
    doReturn(null).when(userRepository).findByNickname(user.getNickname());

    UserDTO newUser = user.createNotPermittedUserWithEmailToken();
    newUser.changeHashedPassword(passwordEncoder);

    doNothing().when(userRepository).save(any(UserDTO.class));
    doNothing().when(redisStrategy).addEmailToken(any(UserDTO.class));
    doNothing().when(emailStrategy).sendUserJoinMessage(any(UserDTO.class));

    userService.join(user);

    verify(userRepository, times(1)).findByEmail(user.getEmail());
    verify(userRepository, times(1)).findByNickname(user.getNickname());
    verify(userRepository, times(1)).save(any(UserDTO.class));
    verify(redisStrategy, times(1)).addEmailToken(any(UserDTO.class));
    verify(emailStrategy, times(1)).sendUserJoinMessage(any(UserDTO.class));

    assertThat(passwordEncoder.matches(user.getPassword(), newUser.getPassword())).isTrue();
  }

  @Test
  @DisplayName("회원가입 실패 : 이메일 중복")
  public void joinFailByEmailDuplicate() {

    UserDTO user = joinUser();

    doReturn(user).when(userRepository).findByEmail(user.getEmail());

    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(EmailDuplicatedException.class);
  }

  @Test
  @DisplayName("회원가입 실패 : 닉네임 중복")
  public void joinFailByNicknameDuplicate() {

    UserDTO user = joinUser();

    doReturn(null).when(userRepository).findByEmail(user.getEmail());
    doReturn(user).when(userRepository).findByNickname(user.getNickname());

    assertThatThrownBy(() -> {
      userService.join(user);
    }).isInstanceOf(NicknameDuplicatedException.class);
  }

  @Test
  @DisplayName("이메일 인증 확인")
  public void checkEmailTokenSuccess() {

    UserDTO user = getUser();

    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(EMAIL_TOKEN);
    doReturn(user).when(userRepository).findByEmail(EMAIL);
    doNothing().when(userRepository).updateRole(user);

    userService.checkEmailToken(EMAIL_TOKEN, EMAIL);

    verify(redisStrategy, times(1)).getValue(EMAIL_TOKEN);
    verify(userRepository, times(1)).findByEmail(EMAIL);
    verify(userRepository, times(1)).updateRole(user);
  }


  @Test
  @DisplayName("이메일 인증 실패")
  public void checkEmailTokenFail() {
    UserDTO user = getUser();

    doReturn("110").when(redisStrategy).getValue(EMAIL_TOKEN);
    doReturn(user).when(userRepository).findByEmail(EMAIL);

    assertThatThrownBy(() -> {
      userService.checkEmailToken(EMAIL_TOKEN, EMAIL);
    }).isInstanceOf(InvalidEmailTokenException.class);
  }

  @Test
  @DisplayName("로그인 성공")
  public void loginSuccess() {

    UserDTO user = loginUser();

    UserDTO findUser = getUser();
    TokenDTO token = getTokenDTO();

    doReturn(findUser).when(userRepository).findByEmail(user.getEmail());
    doReturn(true).when(passwordEncoder).matches(user.getPassword(), findUser.getPassword());
    doReturn(token.getAccessToken()).when(tokenStrategy)
        .createAccessToken(findUser.getId(), findUser.getRole().toString(), findUser.getEmail());
    doReturn(token.getRefreshToken()).when(tokenStrategy).createRefreshToken(findUser.getId());
    doNothing().when(redisStrategy).addRefreshToken(token, findUser);

    assertThat(userService.login(user)).isEqualTo(token);

    verify(userRepository, times(1)).findByEmail(user.getEmail());
    verify(passwordEncoder, times(1)).matches(user.getPassword(), findUser.getPassword());
    verify(tokenStrategy, times(1))
        .createAccessToken(findUser.getId(), findUser.getRole().toString(), findUser.getEmail());
    verify(tokenStrategy, times(1)).createRefreshToken(findUser.getId());
    verify(redisStrategy, times(1)).addRefreshToken(token, findUser);
  }


  @Test
  @DisplayName("로그인 실패 : 패스워드가 일치하지 않음")
  public void loginFail() {

    UserDTO user = loginUser();
    UserDTO findUser = getUser();

    doReturn(findUser).when(userRepository).findByEmail(user.getEmail());
    doReturn(false).when(passwordEncoder).matches(user.getPassword(), findUser.getPassword());

    assertThatThrownBy(() -> {
      userService.login(user);
    }).isInstanceOf(NotMatchedPasswordException.class);
  }


  @Test
  @DisplayName("토큰 재발급 성공")
  public void refreshSuccess() {

    UserDTO user = getUser();
    TokenDTO token = getTokenDTO();

    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(REFRESH_TOKEN);
    doReturn(USER_ID).when(tokenStrategy).getUserId(REFRESH_TOKEN);
    doReturn(user).when(userRepository).findById(USER_ID);
    doReturn(token.getAccessToken()).when(tokenStrategy)
        .createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    doReturn(token.getRefreshToken()).when(tokenStrategy).createRefreshToken(user.getId());

    assertThat(userService.refresh(REFRESH_TOKEN)).isEqualTo(token);

    verify(redisStrategy, times(1)).getValue(REFRESH_TOKEN);
    verify(tokenStrategy, times(1)).getUserId(REFRESH_TOKEN);
    verify(userRepository, times(1)).findById(USER_ID);
    verify(tokenStrategy, times(1))
        .createAccessToken(user.getId(), user.getRole().toString(), user.getEmail());
    verify(tokenStrategy, times(1)).createRefreshToken(user.getId());

  }

  @Test
  @DisplayName("토큰 재발급 실패 : 요청으로 들어온 리프레시 토큰 캐시에 있는 리프레시 토큰과 일치하지 않음")
  public void refreshFailByCache() {

    doReturn(String.valueOf(USER_ID)).when(redisStrategy).getValue(REFRESH_TOKEN);
    doReturn(1000).when(tokenStrategy).getUserId(REFRESH_TOKEN);

    assertThatThrownBy(() -> {
      userService.refresh(REFRESH_TOKEN);
    }).isInstanceOf(NotMatchedIdException.class);
  }

}
