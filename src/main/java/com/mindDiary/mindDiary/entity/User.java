package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

  private int id;

  private String email;

  private String nickname;

  private String password;

  private Role role;

  private String emailCheckToken;

  public User(int id, String email, String nickname, String password, Role role) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.role = role;
  }

  public User(String email, String nickname, String password, Role role, String emailCheckToken) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.role = role;
    this.emailCheckToken = emailCheckToken;
  }

  public void changeHashedPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }

  public void changeRoleUser() {
    this.role = Role.USER;
  }

  public static User create(String email, String nickname, String password, Role role, String emailCheckToken) {

    return new User(
        email,
        nickname,
        password,
        role,
        emailCheckToken);
  }

  public String turnUserinfoToAccessToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createAccessToken(id, role.toString(), email);
  }

  public String turnUserinfoToRefreshToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createRefreshToken(id);
  }
}
