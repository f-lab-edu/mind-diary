package com.mindDiary.mindDiary.exception;

public class NotMatchedPasswordException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_MATCHED_PASSWORD;
  }
}
