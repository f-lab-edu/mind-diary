package com.mindDiary.mindDiary.strategy.cookie;

import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import javax.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CreateCookieStrategy implements CookieStrategy {

  public Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setHttpOnly(true);
    return cookie;
  }
}
