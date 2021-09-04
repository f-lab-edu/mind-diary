package com.mindDiary.mindDiary.strategy.email;

import com.mindDiary.mindDiary.entity.User;

public interface EmailStrategy {
  void sendMessage(String toEmailAddress, String emailCheckToken);

  void sendUserJoinMessage(User user);
}
