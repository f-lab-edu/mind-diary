package com.mindDiary.mindDiary.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.controller.UserController;
import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.service.UserService;
import com.mindDiary.mindDiary.strategy.JwtStrategyTest;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtStrategy jwtStrategy;

  private static final String EMAIL = "email@google.com";

  private static final String PASSWORD = "password";

  private static final String NICKNAME = "nickname";

  private static final String JOIN_URL =  "/auth/join";

  private static final String LOGIN_URL = "/auth/login";

  private static final String CHECK_EMAIL_TOKEN_URL = "/auth/check-email-token";

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

  public User getUser() {
    User user = new User();
    user.setPassword(PASSWORD);
    user.setEmail(EMAIL);
    user.setNickname(NICKNAME);
    return user;
  }

  public UserLoginRequestDTO getUserLoginRequestDTO() {
    UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO();
    userLoginRequestDTO.setPassword(PASSWORD);
    userLoginRequestDTO.setEmail(EMAIL);
    return userLoginRequestDTO;
  }


  @Test
  @DisplayName("회원가입 실패 : 중복 이메일 유저")
  public void joinFailByEmail() throws Exception {
    doReturn(getUser()).when(userService).findByEmail(EMAIL);

    String content = objectMapper.writeValueAsString(getUserJoinRequestDTO());
    ResultActions resultActions = mockMvc
        .perform(post(JOIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isConflict());
  }

  @Test
  @DisplayName("회원가입 실패 : 중복 닉네임 유저")
  public void joinFailByNickname() throws Exception {
    User user = getUser();

    doReturn(null).when(userService).findByEmail(EMAIL);
    doReturn(user).when(userService).findByNickname(NICKNAME);

    String content = objectMapper.writeValueAsString(user);
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

    doReturn(null).when(userService).findByEmail(EMAIL);
    doReturn(null).when(userService).findByNickname(NICKNAME);
    doNothing().when(userService).join(userJoinRequestDTO);

    String content = objectMapper.writeValueAsString(userJoinRequestDTO);
    ResultActions resultActions = mockMvc
        .perform(post(JOIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isOk());
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
    //작성중

  }

  @Test
  @DisplayName("로그인 실패 : 일치하는 유저가 DB에 없을 경우 확인")
  public void loginFailByUserNotExist() throws Exception {

    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    doReturn(null).when(userService).findByEmail(EMAIL);

    String content = objectMapper.writeValueAsString(userLoginRequestDTO);
    ResultActions resultActions = mockMvc
        .perform(post(LOGIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("로그인 실패 : 비밀번호가 틀릴 경우 확인")
  public void loginFailByWrongPassword() throws Exception {

    UserLoginRequestDTO userLoginRequestDTO = getUserLoginRequestDTO();
    User user = getUser();

    doReturn(user).when(userService).findByEmail(EMAIL);
    doReturn(false).when(passwordEncoder).matches(user.getPassword(), userLoginRequestDTO.getPassword());

    String content = objectMapper.writeValueAsString(userLoginRequestDTO);
    ResultActions resultActions = mockMvc
        .perform(post(LOGIN_URL)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isBadRequest());
  }
}
