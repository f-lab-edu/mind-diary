package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDTO {

  private String accessToken;
  private String refreshToken;

  public TokenResponseDTO(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public Cookie createTokenCookie(CookieStrategy cookieStrategy, String key) {
    return cookieStrategy.createCookie(key, refreshToken);
  }

  public AccessTokenResponseDTO createAccessTokenResponseDTO() {
    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
    accessTokenResponseDTO.setAccessToken(accessToken);
    return accessTokenResponseDTO;
  }
}
