package com.mindDiary.mindDiary.strategy.redis;


public interface RedisStrategy {

  void setValudData(String key, String value);

  String getValueData(String key);

  void setValueExpire(String key, String value, long duration);

  void deleteValue(String key);
}
