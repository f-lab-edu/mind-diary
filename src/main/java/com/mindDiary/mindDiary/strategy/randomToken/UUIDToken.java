package com.mindDiary.mindDiary.strategy.randomToken;

import com.mindDiary.mindDiary.exception.businessException.InvalidEmailTokenException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UUIDToken implements RandomTokenGenerator {

  @Override
  public String create() {
    UUID result = UUID.randomUUID();
    valid(result);
    return result.toString();
  }

  private void valid(UUID result) {
    if (result == null || result.toString().isEmpty() || result.toString().isBlank()) {
      throw new InvalidEmailTokenException();
    }
  }
}
