package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class PermissionDeniedException extends
    BusinessException {
  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.PERMISSION_DENIED_EXCEPTION;
  }
}
