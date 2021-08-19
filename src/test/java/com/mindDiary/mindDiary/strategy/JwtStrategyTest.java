package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtStrategyTest {

  @Autowired
  JwtStrategy jwtUtil;

  @Test
  @DisplayName("token으로 userId 가져오는지 확인")
  public void getUserIdByToken() {
    String token = jwtUtil.createToken(35,1,"meme@naver.com",600);
    assertThat(jwtUtil.getUserId(token)).isSameAs(35);
  }

  @Test
  @DisplayName("token으로 userRole 가져오는지 확인")
  public void getUserRoleByToken() {
    String token = jwtUtil.createToken(1,1,"meme@naver.com",600);
    assertThat(jwtUtil.getUserRole(token)).isSameAs(5);
  }

  @Test
  @DisplayName("token으로 이메일 가져오는지 확인")
  public void getUserEmailByToken() {
    String email = "meme@naver.com";
    String token = jwtUtil.createToken(1,1,email,600);
    assertThat(jwtUtil.getUserEmail(token)).isEqualTo(email);
  }

  @Test
  @DisplayName("토큰 유효성 확인")
  public void validateToken() {
    String token = jwtUtil.createToken(1, 1,"meme@naver.com", 0);
    assertThat(jwtUtil.validateToken(token)).isFalse();
  }

}
