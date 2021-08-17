package com.mindDiary.mindDiary.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
  private Integer id;
  private String nickname;
  private String email;
  private String password;
  private Integer role;
}
