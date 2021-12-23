package com.mindDiary.mindDiary.exception.businessException;


import com.mindDiary.mindDiary.exception.ErrorCode;
public class InvalidPasswordException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.INVALID_PASSWORD_EXCEPTION;
  }
}