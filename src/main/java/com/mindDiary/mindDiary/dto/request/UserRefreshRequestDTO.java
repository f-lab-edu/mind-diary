package com.mindDiary.mindDiary.dto.request;

import javax.validation.constraints.NotNull;

public class UserRefreshRequestDTO {

  @NotNull
  String refreshToken;
}
