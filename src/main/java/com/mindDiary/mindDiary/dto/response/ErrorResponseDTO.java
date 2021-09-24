package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
  private int status;
  private String message;

  public static ErrorResponseDTO of(ErrorCode errorCode) {

    return new ErrorResponseDTO(
        errorCode.getStatus(),
        errorCode.getMessage());
  }

  public static ErrorResponseDTO of(int status, String message) {

    return new ErrorResponseDTO(status, message);
  }
}
