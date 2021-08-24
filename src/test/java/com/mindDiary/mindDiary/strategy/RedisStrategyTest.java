package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisStrategyTest {

  @Autowired
  RedisStrategy redisStrategy;

  @Test
  @DisplayName("redis 키 값 저장 테스트")
  public void redisSetValue() {
    String email = "meme@naver.com";
    User user = new User();
    user.setEmail(email);
    String uuid = UUID.randomUUID().toString();

    redisStrategy.setValudData(uuid, user.getEmail());

    assertThat(redisStrategy.getValueData(uuid)).isEqualTo(email);
  }
}
