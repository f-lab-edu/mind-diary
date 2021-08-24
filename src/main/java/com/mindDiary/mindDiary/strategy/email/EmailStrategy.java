package com.mindDiary.mindDiary.strategy.email;

public interface EmailStrategy {
  void sendMessage(String toEmailAddress, String emailCheckToken);
}
