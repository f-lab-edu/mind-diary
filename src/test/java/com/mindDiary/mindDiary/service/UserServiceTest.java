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
import com.mindDiary.mindDiary.repository.UserRepository;
import com.mindDiary.mindDiary.utils.RedisUtil;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Spy
  private PasswordEncoder passwordEncoder;

  @Spy
  private RedisUtil redisUtil;

  @Spy
  private EmailService emailService;

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
  @DisplayName("중복 이메일 확인")
  void isEmailDuplicate() {
    User user = new User();
    user.setPassword("new");
    user.setEmail("new@naver.com");
    user.setNickname("구우");
    doReturn(user).when(userRepository).findByEmail(user.getEmail());

    boolean isDuplicated = userService.isEmailDuplicate(user.getEmail());

    assertThat(isDuplicated).isTrue();
  }

  @Test
  @DisplayName("중복 닉네임 확인")
  void isNicknameDuplicate() {
    User user = new User();
    user.setPassword("new");
    user.setEmail("new@naver.com");
    user.setNickname("구우");
    doReturn(user).when(userRepository).findByNickname(user.getNickname());

    boolean isDuplicated = userService.isNicknameDuplicate(user.getNickname());

    assertThat(isDuplicated).isTrue();
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
