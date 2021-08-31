package com.mindDiary.mindDiary.dto;

import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import javax.servlet.http.Cookie;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class TokenDTO {

  private String accessToken;
  private String refreshToken;

  public static TokenDTO create(UserDTO user, TokenStrategy tokenStrategy) {
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setAccessToken(
        tokenStrategy.createAccessToken(user.getId(), user.getRole().toString(), user.getEmail()));
    tokenDTO.setRefreshToken(tokenStrategy.createRefreshToken(user.getId()));
    return tokenDTO;
  }
}
