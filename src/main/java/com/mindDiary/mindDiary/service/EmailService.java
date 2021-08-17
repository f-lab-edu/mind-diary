package com.mindDiary.mindDiary.service;

public interface EmailService {
  void sendMessage(String toEmailAddress, String emailCheckToken);
}
