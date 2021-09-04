package com.mindDiary.mindDiary.strategy.redis;


import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;

public interface RedisStrategy {

  void setValue(String key, String value);

  String getValue(String key);

  void setValue(String key, String value, long duration);

  void deleteValue(String key);

  void validateValue(String key, String value);

  void validateDuration(long duration);

  void addRefreshToken(Token token, User user);

  void addEmailToken(User user);

}
