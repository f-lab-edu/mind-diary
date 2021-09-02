package com.mindDiary.mindDiary.dto;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@ToString
@Getter
@Setter
public class UserDTO {

  private int id;

  @Email
  private String email;

  private String nickname;

  @NotNull
  private String password;

  private Role role;

  private String emailCheckToken;

  public UserDTO createNotPermittedUserWithEmailToken() {
    UserDTO newUserDTO = new UserDTO();
    newUserDTO.setEmail(email);
    newUserDTO.setNickname(nickname);
    newUserDTO.setPassword(password);
    newUserDTO.setRole(Role.NOT_PERMITTED);
    newUserDTO.setEmailCheckToken(UUID.randomUUID().toString());
    return newUserDTO;
  }

  public void changeHashedPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }

  public void changeRoleUser() {
    this.role = Role.USER;
  }
}
