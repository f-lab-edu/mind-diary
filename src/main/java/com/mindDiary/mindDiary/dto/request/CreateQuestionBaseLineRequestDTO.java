package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionBaseLineRequestDTO {

  @NotNull
  private String content;
  @NotNull
  private int score;

  public QuestionBaseLine createEntity(int diagnosisId) {
    return QuestionBaseLine.builder()
        .content(content)
        .score(score)
        .diagnosisId(diagnosisId)
        .build();
  }
}
