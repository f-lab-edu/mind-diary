package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;
import com.mindDiary.mindDiary.exception.businessException.BusinessException;

public class NicknameDuplicatedException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NICKNAME_DUPLICATED;
  }
}
