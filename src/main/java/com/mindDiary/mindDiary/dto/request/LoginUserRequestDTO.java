package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequestDTO {

  @Email
  private String email;

  @NotNull
  private String password;

  public User turnIntoUserEntity() {
    return User.create(email, password);
  }
}
