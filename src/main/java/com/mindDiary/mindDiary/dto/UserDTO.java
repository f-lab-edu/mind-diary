package com.mindDiary.mindDiary.dto;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

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

  public enum Role {
    NOT_PERMITTED, USER, ADMIN;
  }

  public static UserDTO createNotPermittedUserWithEmailToken(UserDTO userDTO) {
    UserDTO newUserDTO = new UserDTO();
    newUserDTO.setEmail(userDTO.getEmail());
    newUserDTO.setNickname(userDTO.getNickname());
    newUserDTO.setPassword(userDTO.getPassword());
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
