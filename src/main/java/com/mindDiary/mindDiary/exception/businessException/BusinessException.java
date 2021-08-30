package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class BusinessException extends RuntimeException {

  public ErrorCode getErrorCode() {
    return ErrorCode.BUSINESS_EXCEPTION;
  }
}