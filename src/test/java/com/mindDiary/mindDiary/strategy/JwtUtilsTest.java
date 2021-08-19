package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilsTest {

  @Autowired
  JwtStrategy jwtUtil;

  @Test
  @DisplayName("token으로 userId 가져오는지 확인")
  public void getUserIdByToken() {
    String token = jwtUtil.createToken(35,1,600);
    assertThat(jwtUtil.getUserId(token)).isSameAs(35);
  }

  @Test
  @DisplayName("token으로 userRole 가져오는지 확인")
  public void getUserRoleByToken() {
    String token = jwtUtil.createToken(1,5,600);
    assertThat(jwtUtil.getUserRole(token)).isSameAs(5);
  }

}
