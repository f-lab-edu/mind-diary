package com.mindDiary.mindDiary.domain;

public enum UserRole {
  ROLE_NOT_PERMITTED(0),
  ROLE_USER(1),
  ROLE_ADMIN(2);

  private int role;

  UserRole(int role) {
    this.role = role;
  }

  public int getRole() {
    return role;
  }
}
