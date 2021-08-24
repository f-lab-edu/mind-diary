package com.mindDiary.mindDiary.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class UserJoinRequestDTO {
  @Email
  private String email;

  @NotNull
  private String nickname;

  @NotNull
  private String password;

  public void changePassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }
}
