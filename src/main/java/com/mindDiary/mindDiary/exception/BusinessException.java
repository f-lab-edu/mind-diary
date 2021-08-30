package com.mindDiary.mindDiary.exception;

public class BusinessException extends RuntimeException {

  public ErrorCode getErrorCode() {
    return ErrorCode.BUSINESS_EXCEPTION;
  }
}