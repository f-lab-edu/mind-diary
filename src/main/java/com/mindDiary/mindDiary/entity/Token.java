package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
import javax.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

  private String accessToken;

  private String refreshToken;

  public static Token create(User user, TokenStrategy tokenStrategy) {

    return new Token(
        user.turnUserinfoToAccessToken(tokenStrategy),
        user.turnUserinfoToRefreshToken(tokenStrategy));
  }

  public Cookie turnRefreshTokenInfoCookie(CookieStrategy cookieStrategy) {
    return cookieStrategy.createRefreshTokenCookie(refreshToken);
  }

}
