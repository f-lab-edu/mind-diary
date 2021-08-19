package com.mindDiary.mindDiary.strategy;

import javax.servlet.http.Cookie;

public interface CookieStrategy {
  Cookie createCookie(String key, String value);
}
