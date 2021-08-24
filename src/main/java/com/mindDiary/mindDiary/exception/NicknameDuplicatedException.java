package com.mindDiary.mindDiary.exception;

public class NicknameDuplicatedException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NICKNAME_DUPLICATED;
  }
}
