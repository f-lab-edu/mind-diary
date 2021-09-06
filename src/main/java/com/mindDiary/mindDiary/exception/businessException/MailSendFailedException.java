package com.mindDiary.mindDiary.exception.businessException;

import com.mindDiary.mindDiary.exception.ErrorCode;

public class MailSendFailedException extends
    BusinessException {

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.MAIL_SEND_FAILED_EXCEPTION;
  }
}
