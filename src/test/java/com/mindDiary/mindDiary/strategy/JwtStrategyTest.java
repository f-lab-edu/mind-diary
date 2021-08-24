package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtStrategyTest {

  @Autowired
  JwtStrategy jwtUtil;

  @ParameterizedTest
  @CsvSource(value = {"35, 1, meme@naver.com", "36, 2, meme2@naver.com"})
  @DisplayName("access token으로 userId 가져오는지 확인")
  public void getUserIdByToken(int id, int role, String email) {
    String token = jwtUtil.createAccessToken(id,role,email);
    assertThat(jwtUtil.getUserId(token)).isSameAs(id);
  }

  @ParameterizedTest
  @CsvSource(value = {"35, 1, meme@naver.com", "36, 2, meme2@naver.com"})
  @DisplayName("access token으로 userRole 가져오는지 확인")
  public void getUserRoleByToken(int id, int role, String email) {
    String token = jwtUtil.createAccessToken(id,role,email);
    assertThat(jwtUtil.getUserRole(token)).isSameAs(role);
  }

  @ParameterizedTest
  @CsvSource(value = {"35, 1, meme@naver.com", "36, 2, meme2@naver.com"})
  @DisplayName("access token으로 이메일 가져오는지 확인")
  public void getUserEmailByToken(int id, int role, String email) {
    String token = jwtUtil.createAccessToken(id,role,email);
    assertThat(jwtUtil.getUserEmail(token)).isEqualTo(email);
  }

  @ParameterizedTest
  @ValueSource(ints = {100, 40, 2000})
  @DisplayName("토큰 유효성 성공")
  public void validateTokenSuccess(int seconds) {
    String token1 = jwtUtil.createToken(Jwts.claims(), seconds);
    assertThat(jwtUtil.validateToken(token1)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  @DisplayName("토큰 유효성 실패")
  public void validateTokenFail(int seconds) {
    String token2 = jwtUtil.createToken(Jwts.claims(), seconds);
    assertThat(jwtUtil.validateToken(token2)).isFalse();
  }

}
