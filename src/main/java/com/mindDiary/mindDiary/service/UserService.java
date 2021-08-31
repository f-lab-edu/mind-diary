package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.TokenDTO;
import com.mindDiary.mindDiary.dto.UserDTO;

public interface UserService {

  void join(UserDTO userDTO);

  void checkEmailToken(String token, String email);

  TokenDTO login(UserDTO userDTO);

  TokenDTO refresh(String refreshTokenTakenFromCookie);

  void isNicknameDuplicate(String nickname);

  void isEmailDuplicate(String email);
}
