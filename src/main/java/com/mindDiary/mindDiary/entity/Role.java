package com.mindDiary.mindDiary.entity;

public enum Role {
  NOT_PERMITTED, USER, ADMIN;

  public static boolean isUser(String role) {
    return valueOf(role) == USER;
  }

  public static boolean isAdmin(String role) {
    return valueOf(role) == ADMIN;
  }
}