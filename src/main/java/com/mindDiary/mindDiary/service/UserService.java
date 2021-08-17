package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.request.UserJoinRequestDTO;
import org.springframework.stereotype.Service;

public interface UserService {

  boolean isDuplicate(UserJoinRequestDTO userJoinRequestDTO);

  void join(UserJoinRequestDTO userJoinRequestDTO);
}
