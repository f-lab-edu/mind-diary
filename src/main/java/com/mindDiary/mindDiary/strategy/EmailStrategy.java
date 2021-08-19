package com.mindDiary.mindDiary.strategy;

public interface EmailStrategy {
  void sendMessage(String toEmailAddress, String emailCheckToken);
}
