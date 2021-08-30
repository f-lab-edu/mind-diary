package com.mindDiary.mindDiary.exception;

public class NotMatchedIdException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_MATCHED_ID;
  }
}
