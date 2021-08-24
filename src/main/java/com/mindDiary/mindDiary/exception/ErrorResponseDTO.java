package com.mindDiary.mindDiary.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {
  private int status;
  private String message;

  public static ErrorResponseDTO of(ErrorCode errorCode) {
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
    errorResponseDTO.setStatus(errorCode.getStatus());
    errorResponseDTO.setMessage(errorCode.getMessage());
    return errorResponseDTO;
  }
}
