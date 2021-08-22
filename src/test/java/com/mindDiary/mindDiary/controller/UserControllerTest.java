package com.mindDiary.mindDiary.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.cookie.CreateCookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  private static final String JOIN_URL =  "/auth/join";
  private static final String LOGIN_URL = "/auth/login";
  private static final String CHECK_EMAIL_TOKEN_URL = "/auth/check-email-token";
  private static final String EMAIL = "email@google.com";
  private static final String PASSWORD = "password";
  private static final String NICKNAME = "nickname";
  private static final String ACCESS_TOKEN = "access";
  private static final String REFRESH_TOKEN = "refresh";

  @Autowired
  private MockMvc mockMvc;

  @Spy
  private ObjectMapper objectMapper;

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;

  @Mock
  private CookieStrategy cookieStrategy;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

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

  private TokenResponseDTO getTokenResponseDTO() {
    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(ACCESS_TOKEN, REFRESH_TOKEN);
    return tokenResponseDTO;
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
  @Test
  @DisplayName("회원가입 실패 : 중복 유저")
  public void joinFailByEmail() throws Exception {
    String content = objectMapper.writeValueAsString(getUserJoinRequestDTO());

    doReturn(false).when(userService).join(any(UserJoinRequestDTO.class));

    ResultActions resultActions = mockMvc
        .perform(post(JOIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isConflict());
  }

  @Test
  @DisplayName("회원가입 성공")
  public void join() throws Exception {
    UserJoinRequestDTO userJoinRequestDTO = getUserJoinRequestDTO();
    String content = objectMapper.writeValueAsString(userJoinRequestDTO);

    doReturn(true).when(userService).join(any(UserJoinRequestDTO.class));

    ResultActions resultActions = mockMvc
        .perform(post(JOIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(result -> {
          MockHttpServletResponse response = result.getResponse();
          log.info(response.getContentAsString());
        });

    resultActions.andExpect(status().isOk()).andDo(print());
  }

  @Test
  @DisplayName("인증 메일 전송 성공 : 입력값 정상")
  public void sendMailSuccess() throws Exception {
    String token = "xxxx";
    String email = "xxxx";
    doReturn(true).when(userService).checkEmailToken(token, email);

    ResultActions resultActions = mockMvc
        .perform(get(CHECK_EMAIL_TOKEN_URL)
            .param("token",token)
            .param("email",email));

    resultActions.andDo(print()).andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증 메일 전송 실패 : 입력값 오류")
  public void sendMailFail() throws Exception {
    String token = "xxxx";
    String email = "xxxx";
    doReturn(false).when(userService).checkEmailToken(token, email);

    ResultActions resultActions = mockMvc
        .perform(get(CHECK_EMAIL_TOKEN_URL)
            .param("token",token)
            .param("email",email));

    resultActions.andDo(print()).andExpect(status().isBadRequest());

  }

  @Test
  @DisplayName("로그인 성공")
  public void loginSuccess() throws Exception {
    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    Cookie cookie = getCookie();
    String name = cookie.getName();
    String content = objectMapper.writeValueAsString(userLoginRequestDTO);

    TokenResponseDTO tokenResponseDTO = getTokenResponseDTO();

    doReturn(tokenResponseDTO).when(userService).login(any(UserLoginRequestDTO.class));
    doReturn(cookie).when(cookieStrategy).createCookie(any(), any());

    ResultActions resultActions = mockMvc
        .perform(post(LOGIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isOk()).andExpect(cookie().exists(name));
  }
  /* @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO,
      HttpServletResponse httpServletResponse) {

    TokenResponseDTO tokenResponseDTO = userService.login(userLoginRequestDTO);

    if (tokenResponseDTO == null) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    Cookie cookie = tokenResponseDTO.createTokenCookie(cookieStrategy, cookieRefreshTokenKey);
    httpServletResponse.addCookie(cookie);

    return new ResponseEntity(tokenResponseDTO.createAccessTokenResponseDTO(), HttpStatus.OK);
  }

  User user = userRepository.findByEmail(userLoginRequestDTO.getEmail());
    if (user == null) {
      return null;
    }

    if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
      return null;
    }

    TokenResponseDTO tokenResponseDTO = user.createToken(tokenStrategy);
    redisStrategy.setValueExpire(tokenResponseDTO.getRefreshToken(), user.getEmail(),
        refreshTokenValidityInSeconds);
    return tokenResponseDTO;
*/
  @Test
  @DisplayName("로그인 성공")
  public void loginSuccess2() throws Exception {
    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    Cookie cookie = getCookie();
    String name = cookie.getName();
    String content = objectMapper.writeValueAsString(userLoginRequestDTO);

    TokenResponseDTO tokenResponseDTO = getTokenResponseDTO();

    doReturn(tokenResponseDTO).when(userService).login(any(UserLoginRequestDTO.class));
    //doReturn(cookie).when(cookieStrategy).createCookie(any(), any());
    doReturn(cookie).when(cookieStrategy).createRefreshTokenCookie(tokenResponseDTO.getRefreshToken());

    ResultActions resultActions = mockMvc
        .perform(post(LOGIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isOk()).andExpect(cookie().exists(name));
  }



  @Test
  @DisplayName("로그인 실패")
  public void loginFailByUserNotExist() throws Exception {
    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    String content = objectMapper.writeValueAsString(userLoginRequestDTO);

    doReturn(null).when(userService).login(any(UserLoginRequestDTO.class));

    ResultActions resultActions = mockMvc
        .perform(post(LOGIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isBadRequest());
  }

}
