package com.mindDiary.mindDiary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${mailInfo.email}")
  private String fromEmailAddress;

  public void sendMessage(String toEmailAddress, String emailCheckToken) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmailAddress);
    message.setTo(toEmailAddress);
    message.setSubject("Mind-diary 회원 가입 인증");
    message.setText("/check-email-token?token="+ emailCheckToken +
        "&email=" + toEmailAddress);
    javaMailSender.send(message);
  }
}
