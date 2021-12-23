package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class InvalidPageNumberException extends BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.INVALID_PAGE_NUMBER_EXCEPTION;
  }
}
