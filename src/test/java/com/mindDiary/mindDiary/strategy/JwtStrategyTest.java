package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtStrategyTest {

  @Autowired
  JwtStrategy jwtUtil;

  @Test
  @DisplayName("access token으로 userId 가져오는지 확인")
  public void getUserIdByToken() {
    String token = jwtUtil.createAccessToken(35,1,"meme@naver.com");
    assertThat(jwtUtil.getUserId(token)).isSameAs(35);
  }

  @Test
  @DisplayName("access token으로 userRole 가져오는지 확인")
  public void getUserRoleByToken() {
    String token = jwtUtil.createAccessToken(1,1,"meme@naver.com");
    assertThat(jwtUtil.getUserRole(token)).isSameAs(1);
  }

  @Test
  @DisplayName("access token으로 이메일 가져오는지 확인")
  public void getUserEmailByToken() {
    String email = "meme@naver.com";
    String token = jwtUtil.createAccessToken(1,1,email);
    assertThat(jwtUtil.getUserEmail(token)).isEqualTo(email);
  }

  @Test
  @DisplayName("토큰 유효성 확인")
  public void validateToken() {
    String token1 = jwtUtil.createToken(Jwts.claims(), 100);
    assertThat(jwtUtil.validateToken(token1)).isTrue();

    String token2 = jwtUtil.createToken(Jwts.claims(), 0);
    assertThat(jwtUtil.validateToken(token2)).isFalse();
  }

}
