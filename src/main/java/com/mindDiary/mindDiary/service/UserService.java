package com.mindDiary.mindDiary.service;


import com.mindDiary.mindDiary.domain.User;

public interface UserService {

  boolean isEmailDuplicate(String email);

  boolean isNicknameDuplicate(String nickname);

  void join(User user);

  boolean checkEmailToken(String token, String email);
}
