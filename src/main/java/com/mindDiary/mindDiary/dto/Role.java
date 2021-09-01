package com.mindDiary.mindDiary.dto;

import lombok.extern.slf4j.Slf4j;

public enum Role {
  NOT_PERMITTED, USER, ADMIN;

  public static boolean isUser(String role) {
    return valueOf(role) == USER;
  }

  public static boolean isAdmin(String role) {
    return valueOf(role) == ADMIN;
  }
}