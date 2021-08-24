package com.mindDiary.mindDiary.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ErrorResponseDTO response = ErrorResponseDTO.of(ErrorCode.INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataAccessException.class)
  protected ResponseEntity<ErrorResponseDTO> dataAccessException(DataAccessException e) {
    ErrorResponseDTO response = ErrorResponseDTO.of(ErrorCode.DATA_ACCESS_EXCEPTION);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponseDTO> handleBusinessException(BusinessException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.info(errorCode.getMessage());
    ErrorResponseDTO response = ErrorResponseDTO.of(errorCode);
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }

  @ExceptionHandler(InvalidJwtException.class)
  protected ResponseEntity<ErrorResponseDTO> handleBusinessException(InvalidJwtException e) {
    ErrorResponseDTO response = ErrorResponseDTO.of(HttpStatus.FORBIDDEN.value(), e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

}
