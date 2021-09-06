package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
public class User {

  private int id;

  private String email;

  private String nickname;

  private String password;

  private Role role;

  private String emailCheckToken;

  public User createNotPermittedUserWithEmailToken() {
    User newUser = new User();
    newUser.setEmail(email);
    newUser.setNickname(nickname);
    newUser.setPassword(password);
    newUser.setRole(Role.NOT_PERMITTED);
    newUser.setEmailCheckToken(UUID.randomUUID().toString());
    return newUser;
  }

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
    return user;
  }

  public static User create(String email, String password) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    return user;
  }

  public String turnUserinfoToAccessToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createAccessToken(id, role.toString(), email);
  }

  public String turnUserinfoToRefreshToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createRefreshToken(id);
  }
}
