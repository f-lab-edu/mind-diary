package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.User;
import java.util.UUID;
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
public class JoinUserRequestDTO {

  @Email
  private String email;

  @NotNull
  private String nickname;

  @NotNull
  private String password;

  public User createEntity() {
    return User.create(
        email,
        nickname,
        password,
        Role.NOT_PERMITTED,
        UUID.randomUUID().toString());
  }
}
