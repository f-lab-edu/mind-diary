package com.mindDiary.mindDiary.domain;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;

  private String email;

  private String nickname;

  private String password;

  private String role;

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

  public void changeRoleUser() {
    this.role = UserRole.ROLE_USER.getRole();
  }

  public TokenResponseDTO createToken(TokenStrategy tokenStrategy) {

    String accessToken = tokenStrategy.createAccessToken(id, role, email);
    String refreshToken = tokenStrategy.createRefreshToken(id);
    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(accessToken, refreshToken);
    return tokenResponseDTO;
  }
}
