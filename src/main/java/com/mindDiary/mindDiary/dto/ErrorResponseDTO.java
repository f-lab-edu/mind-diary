package com.mindDiary.mindDiary.dto;

import com.mindDiary.mindDiary.exception.ErrorCode;
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

  public static ErrorResponseDTO of(int status, String message) {
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
    errorResponseDTO.setStatus(status);
    errorResponseDTO.setMessage(message);
    return errorResponseDTO;
  }
}
