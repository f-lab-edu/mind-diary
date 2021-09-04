package com.mindDiary.mindDiary.strategy.redis;

import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.RedisAddValueException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StringRedisStrategy implements RedisStrategy {

  @Value("${mailInfo.email-validity-in-seconds}")
  private long emailValidityInSeconds;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  private static final int ZERO = 0;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Override
  public void setValue(String key, String value) {
    validateValue(key, value);
    redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public String getValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void setValue(String key, String value, long duration) {
    validateDuration(duration);
    validateValue(key, value);
    Duration expireDuration = Duration.ofSeconds(duration);
    redisTemplate.opsForValue().set(key, value, expireDuration);
  }

  @Override
  public void validateValue(String key, String value) {
    if (key == null || key.length() == ZERO || value == null || value.length() == ZERO) {
      throw new RedisAddValueException();
    }
  }

  @Override
  public void validateDuration(long duration) {
    if (duration <= ZERO) {
      throw new RedisAddValueException();
    }
  }

  @Override
  public void deleteValue(String key) {
    redisTemplate.delete(key);
  }

  @Override
  public void addEmailToken(User user) {
    setValue(user.getEmailCheckToken(), String.valueOf(user.getId()), emailValidityInSeconds);
  }


  @Override
  public void addRefreshToken(Token token, User user) {
    setValue(token.getRefreshToken(), String.valueOf(user.getId()),
        refreshTokenValidityInSeconds);
  }
}
