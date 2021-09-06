package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.dto.response.AccessTokenResponseDTO;
import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

  private String accessToken;

  private String refreshToken;

  public static Token create(User user, TokenStrategy tokenStrategy) {
    Token token = new Token();
    token.setAccessToken(user.turnUserinfoToAccessToken(tokenStrategy));
    token.setRefreshToken(user.turnUserinfoToRefreshToken(tokenStrategy));
    return token;
  }

  public Cookie turnRefreshTokenInfoCookie(CookieStrategy cookieStrategy) {
    return cookieStrategy.createRefreshTokenCookie(refreshToken);
  }

  public AccessTokenResponseDTO turnIntoAccessTokenResponseDTO() {
    return AccessTokenResponseDTO.create(accessToken);
  }
}
