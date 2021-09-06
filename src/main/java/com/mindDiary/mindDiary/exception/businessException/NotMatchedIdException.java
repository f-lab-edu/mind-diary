package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;
import com.mindDiary.mindDiary.exception.businessException.BusinessException;

public class NotMatchedIdException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_MATCHED_ID;
  }
}
