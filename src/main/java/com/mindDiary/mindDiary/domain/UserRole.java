package com.mindDiary.mindDiary.domain;

public enum UserRole {
  ROLE_NOT_PERMITTED("NOT_PERMITTED"),
  ROLE_USER("USER"),
  ROLE_ADMIN("ADMIN");

  private String role;

  UserRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
