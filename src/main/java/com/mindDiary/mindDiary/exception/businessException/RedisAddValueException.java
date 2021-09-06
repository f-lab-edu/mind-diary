package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class RedisAddValueException extends
    BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.REDIS_ADD_VALUE_EXCEPTION;
  }
}
