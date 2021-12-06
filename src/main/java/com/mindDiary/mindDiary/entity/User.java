package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import com.mindDiary.mindDiary.strategy.randomToken.RandomTokenGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

  public String turnUserinfoToAccessToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createAccessToken(id, role.toString(), email);
  }

  public String turnUserinfoToRefreshToken(TokenStrategy tokenStrategy) {
    return tokenStrategy.createRefreshToken(id);
  }

  public void createEmailToken(RandomTokenGenerator generator) {
    this.emailCheckToken = generator.create();
  }
}
