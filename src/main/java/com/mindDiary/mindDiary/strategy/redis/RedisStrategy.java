package com.mindDiary.mindDiary.strategy.redis;

import java.time.Duration;

public interface RedisStrategy {

  void setValudData(String key, String value);

  String getValueData(String key);

  void setValueExpire(String key, String value, long duration);

  void deleteValue(String key);
}
