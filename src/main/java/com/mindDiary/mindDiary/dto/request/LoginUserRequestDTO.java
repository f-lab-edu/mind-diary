package com.mindDiary.mindDiary.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequestDTO {

  @Email
  private String email;

  @NotNull
  private String password;

}
