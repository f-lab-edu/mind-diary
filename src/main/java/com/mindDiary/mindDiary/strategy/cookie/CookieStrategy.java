package com.mindDiary.mindDiary.strategy.cookie;

import javax.servlet.http.Cookie;

public interface CookieStrategy {
  Cookie createCookie(String key, String value);
}
