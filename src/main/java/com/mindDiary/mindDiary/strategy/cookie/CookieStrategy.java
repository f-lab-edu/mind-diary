package com.mindDiary.mindDiary.strategy.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieStrategy {

  Cookie createCookie(String key, String value);

  Cookie getCookie(String key, HttpServletRequest httpServletRequest);

  void deleteCookie(String key, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
