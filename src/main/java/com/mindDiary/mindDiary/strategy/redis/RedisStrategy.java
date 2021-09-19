package com.mindDiary.mindDiary.strategy.redis;

public interface RedisStrategy {

  void setValue(String key, String value);

  String getValue(String key);

  void setValue(String key, String value, long duration);

  void deleteValue(String key);

  void validateValue(String key, String value);

  void validateDuration(long duration);

  void addEmailToken(String emailCheckToken, int userId);

  void addRefreshToken(String refreshToken, int userId);
}
