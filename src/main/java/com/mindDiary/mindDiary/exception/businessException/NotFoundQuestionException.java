package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class NotFoundQuestionException extends BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_FOUND_QUESTION;
  }
}
