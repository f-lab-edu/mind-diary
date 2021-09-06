package com.mindDiary.mindDiary.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponseDTO {
  String accessToken;

  public static AccessTokenResponseDTO create(String accessToken) {
    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
    accessTokenResponseDTO.setAccessToken(accessToken);
    return accessTokenResponseDTO;
  }
}
