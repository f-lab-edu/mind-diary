package com.mindDiary.mindDiary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenDTO {
  String accessToken;

  public static AccessTokenDTO create(String accessToken) {
    AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
    accessTokenDTO.setAccessToken(accessToken);
    return accessTokenDTO;
  }
}