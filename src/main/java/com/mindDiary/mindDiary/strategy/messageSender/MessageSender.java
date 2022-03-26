package com.mindDiary.mindDiary.strategy.messageSender;


public interface MessageSender {
  void sendMessage(String toEmailAddress, String emailCheckToken);
}
