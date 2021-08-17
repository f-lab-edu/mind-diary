package com.mindDiary.mindDiary.service;


import com.mindDiary.mindDiary.domain.User;

public interface UserService {

  boolean isDuplicate(User user);

  void join(User user);

  boolean checkEmailToken(String token, String email);
}
