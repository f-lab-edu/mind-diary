package com.mindDiary.mindDiary.service;


import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import com.mindDiary.mindDiary.dto.request.UserLoginRequestDTO;
import com.mindDiary.mindDiary.dto.response.TokenResponseDTO;

public interface UserService {

  void join(UserJoinRequestDTO userJoinRequestDTO);

  boolean checkEmailToken(String token, String email);

  TokenResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);

  TokenResponseDTO refresh(String originToken);
}
