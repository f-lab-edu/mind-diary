package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

  @Autowired
  PasswordEncoder passwordEncoder;

  @ParameterizedTest
  @ValueSource(strings = {"meme","meme2"})
  @DisplayName("bcrypt 비밀번호 생성 및 매칭 테스트")
  public void passwordEncoder(String password) {
    String encodePassword = passwordEncoder.encode(password);
    assertThat(passwordEncoder.matches(password, encodePassword)).isTrue();
  }
}
