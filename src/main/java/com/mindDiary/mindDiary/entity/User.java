package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@ToString
@Setter
@Getter
public class User {

  private int id;

  private String email;

  private String nickname;

  private String password;

  private Role role;

  private String emailCheckToken;

  public void changeHashedPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(password);
  }

  public void changeRoleUser() {
    this.role = Role.USER;
  }

  public static User create(String email, String nickname, String password) {
    User user = new User();
    user.setEmail(email);
    user.setNickname(nickname);
    user.setPassword(password);
    user.setRole(Role.NOT_PERMITTED);
    user.setEmailCheckToken(UUID.randomUUID().toString());
    return user;
  }

  public String turnUserinfoToAccessToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createAccessToken(id, role.toString(), email);
  }

  public String turnUserinfoToRefreshToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createRefreshToken(id);
  }
}
