package com.mindDiary.mindDiary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindDiary.mindDiary.controller.UserController;
import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.utils.RedisUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  RedisUtil redisUtil;

  @Autowired
  EmailService emailService;

  @Value("${mailInfo.email}")
  private String email;


  @Test
  @DisplayName("bcrypt 비밀번호 생성 및 매칭 테스트")
  public void passwordEncoder() {
    String encodePassword = passwordEncoder.encode("meme");
    System.out.println(encodePassword);

    assertThat(passwordEncoder.matches("meme", encodePassword)).isTrue();
  }

  @Test
  @DisplayName("redis 키 값 저장 테스트")
  public void redisSetValue() {
    String email = "meme@naver.com";
    User user = new User();
    user.setEmail(email);
    String uuid = UUID.randomUUID().toString();

    redisUtil.setValudData(uuid, user.getEmail());

    assertThat(redisUtil.getValueData(uuid)).isEqualTo(email);
  }

  @Test
  @DisplayName("인증 메일 전송 테스트")
  public void sendMail() {
    String uuid = UUID.randomUUID().toString();
    emailService.sendMessage(email, uuid);
  }

}
