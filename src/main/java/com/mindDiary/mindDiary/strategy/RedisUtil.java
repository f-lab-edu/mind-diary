package com.mindDiary.mindDiary.strategy;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisUtil {

  @Autowired
  private StringRedisTemplate redisTemplate;

  public void setValudData(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public String getValueData(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void setValueExpire(String key, String value, long duration) {
    Duration expireDuration = Duration.ofSeconds(duration);
    redisTemplate.opsForValue().set(key,value,expireDuration);
  }

  public void deleteValue(String key) {
    redisTemplate.delete(key);
  }
}
