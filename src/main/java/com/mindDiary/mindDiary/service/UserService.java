package com.mindDiary.mindDiary.service;


import com.mindDiary.mindDiary.domain.User;
import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;

public interface UserService {

  boolean join(UserJoinRequestDTO userJoinRequestDTO);

  boolean checkEmailToken(String token, String email);

  User findByEmail(String email);

  User findByNickname(String nickname);

}
