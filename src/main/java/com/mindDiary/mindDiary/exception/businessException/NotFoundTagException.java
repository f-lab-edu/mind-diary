package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class NotFoundTagException extends BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_FOUND_TAG;
  }
}