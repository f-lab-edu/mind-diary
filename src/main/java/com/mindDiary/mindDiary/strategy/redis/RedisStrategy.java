package com.mindDiary.mindDiary.strategy.redis;


public interface RedisStrategy {

  void setValue(String key, String value);

  String getValue(String key);

  void setValue(String key, String value, long duration);

  void deleteValue(String key);
}
