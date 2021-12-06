package com.mindDiary.mindDiary.strategy.randomToken;

import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UUIDToken implements RandomTokenGenerator {

  @Override
  public String create() {
    return UUID.randomUUID().toString();
  }
}
