package com.mindDiary.mindDiary.service;


import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.response.UserLoginResponseDTO;

public interface UserService {

  boolean isEmailDuplicate(String email);

  boolean isNicknameDuplicate(String nickname);

  void join(User user);

  boolean checkEmailToken(String token, String email);

  User findByEmail(String email);

  boolean passwordMatches(String password, String password1);
}
