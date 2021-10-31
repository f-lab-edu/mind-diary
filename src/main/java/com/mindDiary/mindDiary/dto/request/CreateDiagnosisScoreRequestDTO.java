package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiagnosisScoreRequestDTO {

  @NotNull
  private int minValue;
  @NotNull
  private int maxValue;
  @NotNull
  private String content;

  public DiagnosisScore createEntity(int diagnosisId) {
    return DiagnosisScore.builder()
        .diagnosisId(diagnosisId)
        .minValue(minValue)
        .maxValue(maxValue)
        .content(content)
        .build();
  }
}
