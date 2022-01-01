package com.mindDiary.mindDiary.strategy.messageSender;

import com.mindDiary.mindDiary.exception.businessException.MailSendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.Loader.Simple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailMessage implements MessageSender {

  @Value("${mailInfo.email}")
  private final String fromEmailAddress;
  private static final String URL = "http://localhost:8080/auth/check-email-token";
  private static final String SUBJECT = "Mind-diary 회원 가입 인증";
  private static final int ZERO = 0;
  private final JavaMailSender javaMailSender;

  @Override
  public void sendMessage(String toEmailAddress, String text) {
    validateString(toEmailAddress);
    validateString(text);
    SimpleMailMessage message = createMessage(toEmailAddress, text);
    javaMailSender.send(message);
  }
  private SimpleMailMessage createMessage(String toEmailAddress, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmailAddress);
    message.setTo(toEmailAddress);
    message.setSubject(SUBJECT);
    message.setText(URL + text);
    return message;
  }


  public void validateString(String str) {
    if (str == null || str.length() == ZERO) {
      throw new MailSendFailedException();
    }
  }

}
