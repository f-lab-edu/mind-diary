package com.mindDiary.mindDiary.exception;

public class EmailDuplicatedException extends BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.EMAIL_DUPLICATED;
  }
}
