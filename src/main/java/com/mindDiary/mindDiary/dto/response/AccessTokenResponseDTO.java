package com.mindDiary.mindDiary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponseDTO {

  String accessToken;

  public static AccessTokenResponseDTO create(String accessToken) {

    return new AccessTokenResponseDTO(accessToken);
  }
}
