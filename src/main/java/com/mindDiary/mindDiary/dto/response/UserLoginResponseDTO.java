package com.mindDiary.mindDiary.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponseDTO {
  String accessToken;
  String refreshToken;
}
