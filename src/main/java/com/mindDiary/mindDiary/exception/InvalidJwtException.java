package com.mindDiary.mindDiary.exception;

public class InvalidJwtException extends RuntimeException {

  public InvalidJwtException(Throwable e) {
    super(e);
  }

  public InvalidJwtException(String message) {
    super(message);
  }

  public InvalidJwtException() {
    super();
  }
}
