package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiagnosisRequestDTO {


  @NotNull
  private String name;
  @NotNull
  private int numberOfChoice;
  @NotNull
  private List<CreateQuestionRequestDTO> questions;
  @NotNull
  private List<CreateQuestionBaseLineRequestDTO> questionBaseLines;
  @NotNull
  private List<CreateDiagnosisScoreRequestDTO> diagnosisScores;


  public Diagnosis createDiagnosisEntity() {
    return Diagnosis.builder()
        .name(name)
        .numberOfChoice(numberOfChoice)
        .build();
  }

  public List<DiagnosisScore> createDiagnosisScoreEntityList(int diagnosisId) {
    return Optional.ofNullable(diagnosisScores)
        .orElse(new ArrayList<>())
        .stream()
        .map(ds -> ds.createEntity(diagnosisId))
        .collect(Collectors.toList());


  }

  public List<Question> createQuestionEntityList(int diagnosisId) {
    return Optional.ofNullable(questions)
        .orElse(new ArrayList<>())
        .stream()
        .map(q -> q.createEntity(diagnosisId))
        .collect(Collectors.toList());
  }

  public List<QuestionBaseLine> createQuestionBaseLineEntityList(int diagnosisId) {
    return Optional.ofNullable(questionBaseLines)
        .orElse(new ArrayList<>())
        .stream()
        .map(qb -> qb.createEntity(diagnosisId))
        .collect(Collectors.toList());
  }
}
