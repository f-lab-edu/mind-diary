package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;
import com.mindDiary.mindDiary.exception.businessException.BusinessException;

public class InvalidEmailTokenException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.INVALID_EMAIL_TOKEN;
  }
}
