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
    tokenDTO.setAccessToken(user.turnUserinfoToAccessToken(tokenStrategy));
    tokenDTO.setRefreshToken(user.turnUserinfoToRefreshToken(tokenStrategy));
    return tokenDTO;
  }

  public AccessTokenDTO turnAccessTokenIntoAccessTokenDTO() {
    return AccessTokenDTO.create(accessToken);
  }
}
