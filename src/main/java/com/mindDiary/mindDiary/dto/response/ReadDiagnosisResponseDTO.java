package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadDiagnosisResponseDTO {

  private int id;
  private String name;
  private int numberOfChoice;
  private List<QuestionBaseLineDTO> questionBaseLines;
  private List<QuestionDTO> questions;

  public ReadDiagnosisResponseDTO(int id, String name, int numberOfChoice,
      List<Question> questions,
      List<QuestionBaseLine> questionBaseLines) {
    this.id = id;
    this.name = name;
    this.numberOfChoice = numberOfChoice;
    this.questions = questions.stream()
        .map(question -> QuestionDTO.create(question))
        .collect(Collectors.toList());

    this.questionBaseLines = questionBaseLines.stream()
        .map(questionBaseLine -> QuestionBaseLineDTO.create(questionBaseLine))
        .collect(Collectors.toList());
  }

  public static ReadDiagnosisResponseDTO create(Diagnosis diagnosis) {

    return new ReadDiagnosisResponseDTO(
        diagnosis.getId(),
        diagnosis.getName(),
        diagnosis.getNumberOfChoice(),
        diagnosis.getQuestions(),
        diagnosis.getQuestionBaseLines());
  }

}
