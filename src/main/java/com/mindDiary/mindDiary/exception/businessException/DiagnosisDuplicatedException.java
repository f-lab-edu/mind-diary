package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;


public class DiagnosisDuplicatedException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.DIAGNOSIS_DUPLICATED;
  }
}
