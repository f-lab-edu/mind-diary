package com.mindDiary.mindDiary.domain;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;

  @Email
  private String email;

  @NotNull
  private String nickname;

  @NotNull
  private String password;

  private int role;

  private String emailCheckToken;

  public void createEmailCheckToken() {
    this.emailCheckToken = UUID.randomUUID().toString();
  }
}
