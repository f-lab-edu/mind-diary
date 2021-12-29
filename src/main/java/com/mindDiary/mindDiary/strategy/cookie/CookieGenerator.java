package com.mindDiary.mindDiary.strategy.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieGenerator {

  Cookie createCookie(String key, String value, int duration);

  Cookie createRefreshTokenCookie(String value);

  Cookie getCookie(String key, HttpServletRequest httpServletRequest);

  void deleteCookie(String key, HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse);

  Cookie getRefreshTokenCookie(HttpServletRequest httpServletRequest);

  void deleteRefreshTokenCookie(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse);
}
