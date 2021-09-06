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

  private static final String PATH = "/";
  private static final int ZERO = 0;

  public Cookie createCookie(String key, String value, int duration) {
    Cookie cookie = new Cookie(key, value);
    cookie.setHttpOnly(true);
    cookie.setPath(PATH);
    cookie.setMaxAge(duration);
    return cookie;
  }

  public Cookie createRefreshTokenCookie(String value) {
    return createCookie(refreshTokenCookieKey, value, (int) refreshTokenValidityInSeconds);
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
          c.setMaxAge(ZERO);
          c.setPath(PATH);
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
