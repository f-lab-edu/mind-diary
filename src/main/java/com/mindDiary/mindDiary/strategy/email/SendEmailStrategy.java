package com.mindDiary.mindDiary.strategy.email;

import com.mindDiary.mindDiary.strategy.email.EmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailStrategy implements EmailStrategy {

  private static final String URL  = "http://localhost:8080/auth/check-email-token";
  private static final String SUBJECT = "Mind-diary 회원 가입 인증";

  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${mailInfo.email}")
  private String fromEmailAddress;

  public void sendMessage(String toEmailAddress, String emailCheckToken) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmailAddress);
    message.setTo(toEmailAddress);
    message.setSubject(SUBJECT);
    message.setText(URL + "?token="+ emailCheckToken +
        "&email=" + toEmailAddress);
    javaMailSender.send(message);
  }
}
