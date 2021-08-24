package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.strategy.cookie.CookieStrategy;
import javax.servlet.http.Cookie;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class TokenResponseDTO {

  private String accessToken;
  private String refreshToken;

  public TokenResponseDTO(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public Cookie createRefreshTokenCookie(CookieStrategy cookieStrategy) {
    return cookieStrategy.createRefreshTokenCookie(refreshToken);
  }

  public AccessTokenResponseDTO createAccessTokenResponseDTO() {
    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
    accessTokenResponseDTO.setAccessToken(accessToken);
    return accessTokenResponseDTO;
  }
}
