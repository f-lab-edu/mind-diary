package com.mindDiary.mindDiary.strategy.email;

import com.mindDiary.mindDiary.dto.UserDTO;

public interface EmailStrategy {
  void sendMessage(String toEmailAddress, String emailCheckToken);

  void sendUserJoinMessage(UserDTO user);
}
