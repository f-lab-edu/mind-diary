package com.mindDiary.mindDiary.strategy.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface CookieStrategy {
  Cookie createCookie(String key, String value);

  Cookie getCookie(String refreshToken, HttpServletRequest httpServletRequest);
}
