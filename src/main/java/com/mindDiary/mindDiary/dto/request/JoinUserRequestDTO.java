package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JoinUserRequestDTO {

  @Email
  private String email;

  @NotNull
  private String nickname;

  @NotNull
  private String password;

  public User createEntity() {
    return User.create(email, nickname, password);
  }
}
