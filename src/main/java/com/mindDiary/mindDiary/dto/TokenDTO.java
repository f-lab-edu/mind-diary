package com.mindDiary.mindDiary.dto;

import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {

  private String accessToken;
  private String refreshToken;

  public static TokenDTO createToken(UserDTO userDTO, TokenStrategy tokenStrategy) {

    String accessToken = tokenStrategy.createAccessToken(userDTO.getId(),
        userDTO.getRole().toString(),
        userDTO.getEmail());

    String refreshToken = tokenStrategy.createRefreshToken(userDTO.getId());

    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setAccessToken(accessToken);
    tokenDTO.setRefreshToken(refreshToken);
    return tokenDTO;
  }

  public Cookie createRefreshTokenCookie(CookieStrategy cookieStrategy) {
    return cookieStrategy.createRefreshTokenCookie(refreshToken);
  }

  public AccessTokenDTO createAccessTokenDTO() {
    AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
    accessTokenDTO.setAccessToken(accessToken);
    return accessTokenDTO;
  }
}
