package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mindDiary.mindDiary.exception.businessException.RedisAddValueException;
import com.mindDiary.mindDiary.strategy.redis.RedisStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisStrategyTest {

  @Autowired
  RedisStrategy redisStrategy;

  @ParameterizedTest
  @CsvSource(value = {"key, value", "key2, value2"})
  @DisplayName("redis 키 값 저장 테스트")
  public void redisSetValue(String key, String value) {
    redisStrategy.setValue(key, value);
    assertThat(redisStrategy.getValue(key)).isEqualTo(value);
  }

  @ParameterizedTest
  @CsvSource(value = {"key, value,0", "key2, value2, -1"})
  @DisplayName("redis 키 값 저장 실패 : 유효기간이 음수인 경우")
  public void redisSetValue(String key, String value, long duration) {
    assertThatThrownBy(() -> {
      redisStrategy.setValue(key, value, duration);
    }).isInstanceOf(RedisAddValueException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = "aaa")
  @DisplayName("redis 키 값 저장 실패 : 키, 값이 잘못된 경우")
  public void redisSetValue(String str) {
    assertThatThrownBy(() -> {
      redisStrategy.setValue(null, str);
    }).isInstanceOf(RedisAddValueException.class);

    assertThatThrownBy(() -> {
      redisStrategy.setValue(str, null);
    }).isInstanceOf(RedisAddValueException.class);

    assertThatThrownBy(() -> {
      redisStrategy.setValue("", str);
    }).isInstanceOf(RedisAddValueException.class);

    assertThatThrownBy(() -> {
      redisStrategy.setValue(str, "");
    }).isInstanceOf(RedisAddValueException.class);
  }

}
