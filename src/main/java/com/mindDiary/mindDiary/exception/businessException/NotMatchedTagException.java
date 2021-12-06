package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class NotMatchedTagException extends BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_MATCHED_TAG;
  }
}