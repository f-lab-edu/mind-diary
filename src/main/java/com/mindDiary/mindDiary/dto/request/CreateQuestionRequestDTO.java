package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.Reverse;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequestDTO {

  @NotNull
  private String content;
  @NotNull
  private Reverse reverse;

  public Question createEntity(int diagnosisId) {
    return Question.builder()
        .diagnosisId(diagnosisId)
        .content(content)
        .reverse(reverse)
        .build();
  }
}
