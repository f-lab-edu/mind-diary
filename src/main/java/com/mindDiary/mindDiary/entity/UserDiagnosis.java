package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDiagnosis {

  private int userId;
  private int diagnosisId;
  private int score;
  private LocalDateTime createdAt;
  private String content;

  public static UserDiagnosis create(int userId, int diagnosisId, LocalDateTime createdAt, int score, String content) {
    UserDiagnosis userDiagnosis = new UserDiagnosis();
    userDiagnosis.setUserId(userId);
    userDiagnosis.setDiagnosisId(diagnosisId);
    userDiagnosis.setCreatedAt(createdAt);
    userDiagnosis.setScore(score);
    userDiagnosis.setContent(content);
    return userDiagnosis;
  }
}
