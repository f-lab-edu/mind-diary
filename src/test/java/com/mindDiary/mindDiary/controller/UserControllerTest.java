package com.mindDiary.mindDiary.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.controller.UserController;
import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

  @Value("${mailInfo.email}")
  private String email;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @DisplayName("회원가입 실패 : 중복 유저")
  public void joinFail() throws Exception {
    User user = new User();
    user.setPassword("new");
    user.setEmail("new@naver.com");
    user.setNickname("구우");
    doReturn(true).when(userService).isEmailDuplicate(user.getEmail());
    doReturn(false).when(userService).isEmailDuplicate(user.getNickname());

    String content = objectMapper.writeValueAsString(user);
    String url = "/auth/join";
    ResultActions resultActions = mockMvc
        .perform(post(url)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON));

    resultActions.andDo(print()).andExpect(status().isConflict());
  }

  @Test
  @DisplayName("회원가입 성공")
  public void join() throws Exception {
    User user = new User();
    user.setPassword("new");
    user.setEmail("new@naver.com");
    user.setNickname("구우");
    doReturn(false).when(userService).isNicknameDuplicate(user.getNickname());
    doReturn(false).when(userService).isEmailDuplicate(user.getEmail());
    doNothing().when(userService).join(any(User.class));

    String content = objectMapper.writeValueAsString(user);
    String url = "/auth/join";
    ResultActions resultActions = mockMvc
        .perform(post(url)
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

    String url = "/auth/check-email-token";
    ResultActions resultActions = mockMvc
        .perform(get(url)
            .param("token",token)
            .param("email",email));

    resultActions.andDo(print()).andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증 메일 전송 실패 : 입력값 오류")
  public void sendMailFail() throws Exception {
    String url = "/auth/check-email-token";
    String token = "xxxx";
    String email = "xxxx";
    doReturn(false).when(userService).checkEmailToken(token, email);

    ResultActions resultActions = mockMvc
        .perform(get(url)
            .param("token",token)
            .param("email",email));

    resultActions.andDo(print()).andExpect(status().isBadRequest());

  }

}
