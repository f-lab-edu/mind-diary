package com.mindDiary.mindDiary.domain;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.strategy.jwt.JwtStrategy;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;

  private String email;

  private String nickname;

  private String password;

  private int role;

  private String emailCheckToken;

  public static User createUser(UserJoinRequestDTO userJoinRequestDTO) {
    User user = new User();
    user.setEmail(userJoinRequestDTO.getEmail());
    user.setNickname(userJoinRequestDTO.getNickname());
    user.setPassword(userJoinRequestDTO.getPassword());
    user.setRole(UserRole.ROLE_NOT_PERMITTED.getRole());
    user.setEmailCheckToken(UUID.randomUUID().toString());
    return user;
  }

  public static User createUserByToken(int id, int role, String email) {
    User user = new User();
    user.setId(id);
    user.setEmail(email);
    user.setRole(role);
    return user;
  }

  public void changeRoleUser() {
    this.role = UserRole.ROLE_USER.getRole();
  }

  public TokenResponseDTO createToken(JwtStrategy jwtStrategy) {
    String accessToken = jwtStrategy.createAccessToken(id, role, email);
    String refreshToken = jwtStrategy.createRefreshToken(id, role, email);

    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(accessToken, refreshToken);
    return tokenResponseDTO;
  }
}
