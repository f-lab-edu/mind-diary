package com.mindDiary.mindDiary.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDAO {

  @Value("${mailInfo.email-validity-in-seconds}")
  private long emailValidityInSeconds;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;


  public void addEmailToken(String emailCheckToken, int userId) {
    Duration expireDuration = Duration.ofSeconds(emailValidityInSeconds);
    redisTemplate.opsForValue().set(emailCheckToken, String.valueOf(userId), expireDuration);
  }

  public void addRefreshToken(String refreshToken, int userId) {
    Duration expireDuration = Duration.ofSeconds(refreshTokenValidityInSeconds);
    redisTemplate.opsForValue().set(refreshToken, String.valueOf(userId), expireDuration);
  }

  public int getUserId(String originToken) {
    Object userId = redisTemplate.opsForValue().get(originToken);
    return objectMapper.convertValue(userId, Integer.class);
  }

  public void deleteEmailToken(String emailToken) {
    redisTemplate.delete(emailToken);
  }

}
