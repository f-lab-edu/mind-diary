package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Token;
import com.mindDiary.mindDiary.entity.User;

public interface UserService {

  void join(User user);

  void checkEmailToken(String token, String email);

  Token login(User user);

  Token refresh(String refreshTokenTakenFromCookie);

  void isNicknameDuplicate(String nickname);

  void isEmailDuplicate(String email);

}
