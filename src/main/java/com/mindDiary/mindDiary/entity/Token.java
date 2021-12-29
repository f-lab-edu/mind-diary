package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.cookie.CookieGenerator;
import com.mindDiary.mindDiary.strategy.token.TokenGenerator;
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

  public static Token create(User user, TokenGenerator tokenGenerator) {

    return new Token(
        user.turnUserinfoToAccessToken(tokenGenerator),
        user.turnUserinfoToRefreshToken(tokenGenerator));
  }

  public Cookie turnRefreshTokenInfoCookie(CookieGenerator generator) {
    return generator.createRefreshTokenCookie(refreshToken);
  }

}
