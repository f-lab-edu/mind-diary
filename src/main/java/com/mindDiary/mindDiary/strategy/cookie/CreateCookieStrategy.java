package com.mindDiary.mindDiary.strategy.cookie;

import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateCookieStrategy implements CookieStrategy {

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  @Value("${cookie.key.refresh-token}")
  private String refreshTokenCookieKey;

  public Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) refreshTokenValidityInSeconds);
    return cookie;
  }

  public Cookie createRefreshTokenCookie(String value) {
    return createCookie(refreshTokenCookieKey, value);
  }

  @Override
  public Cookie getCookie(String key, HttpServletRequest httpServletRequest) {
    return Arrays.stream(httpServletRequest.getCookies())
        .filter(c -> c.getName().equals(key))
        .findFirst()
        .get();
  }

  @Override
  public void deleteCookie(String key, HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    Arrays.stream(httpServletRequest.getCookies())
        .filter(c -> c.getName().equals(key))
        .findFirst()
        .map(c -> {
          c.setValue(null);
          c.setMaxAge(0);
          c.setPath("/");
          httpServletResponse.addCookie(c);
          return c;
        });
  }

  @Override
  public Cookie getRefreshTokenCookie(HttpServletRequest httpServletRequest) {
    return getCookie(refreshTokenCookieKey, httpServletRequest);
  }

  @Override
  public void deleteRefreshTokenCookie(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    deleteCookie(refreshTokenCookieKey, httpServletRequest, httpServletResponse);
  }
}
