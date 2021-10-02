package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDiagnosis {

  private int userId;
  private int diagnosisId;
  private int score;
  private LocalDateTime createdAt;
  private String content;

  public static UserDiagnosis create(int userId, int diagnosisId, int score, LocalDateTime createdAt,  String content) {

    return UserDiagnosis.builder()
        .userId(userId)
        .diagnosisId(diagnosisId)
        .score(score)
        .createdAt(createdAt)
        .content(content)
        .build();
  }


}
