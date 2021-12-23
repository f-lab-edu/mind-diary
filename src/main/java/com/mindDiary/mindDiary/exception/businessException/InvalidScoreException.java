package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class InvalidScoreException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.INVALID_SCORE_EXCEPTION;
  }
}
