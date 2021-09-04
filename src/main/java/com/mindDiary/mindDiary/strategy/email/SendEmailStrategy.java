package com.mindDiary.mindDiary.strategy.email;

import com.mindDiary.mindDiary.dto.UserDTO;
import com.mindDiary.mindDiary.entity.User;
import com.mindDiary.mindDiary.exception.businessException.MailSendFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailStrategy implements EmailStrategy {

  @Value("${mailInfo.email}")
  private String fromEmailAddress;

  private static final String URL = "http://localhost:8080/auth/check-email-token";
  private static final String SUBJECT = "Mind-diary 회원 가입 인증";
  private static final int ZERO = 0;

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public void sendMessage(String toEmailAddress, String text) {

    validateString(toEmailAddress);
    validateString(text);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmailAddress);
    message.setTo(toEmailAddress);
    message.setSubject(SUBJECT);
    message.setText(URL + text);
    javaMailSender.send(message);

  }


  public void validateString(String str) {
    if (str == null || str.length() == ZERO) {
      throw new MailSendFailedException();
    }
  }

  @Override
  public void sendUserJoinMessage(User user) {
    String text = "?token=" + user.getEmailCheckToken() +
        "&email=" + user.getEmail();
    sendMessage(user.getEmail(), text);
  }

}
