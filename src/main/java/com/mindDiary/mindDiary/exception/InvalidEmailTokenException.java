package com.mindDiary.mindDiary.exception;

public class InvalidEmailTokenException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.INVALID_EMAIL_TOKEN;
  }
}
