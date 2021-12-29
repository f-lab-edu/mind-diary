package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class NotFoundDiagnosisException extends BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.NOT_FOUND_DIAGNOSIS;
  }
}
