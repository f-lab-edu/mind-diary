package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReadDiagnosisResponseDTO {

  private int id;
  private String name;
  private int numberOfChoice;
  private List<QuestionBaseLine> questionBaseLines;
  private List<Question> questions;

  public ReadDiagnosisResponseDTO() {
    questionBaseLines = new ArrayList<>();
    questions = new ArrayList<>();
  }

  public static ReadDiagnosisResponseDTO create(Diagnosis diagnosis) {
    ReadDiagnosisResponseDTO readDiagnosisResponse = new ReadDiagnosisResponseDTO();
    readDiagnosisResponse.setId(diagnosis.getId());
    readDiagnosisResponse.setName(diagnosis.getName());
    readDiagnosisResponse.setNumberOfChoice(diagnosis.getNumberOfChoice());
    return readDiagnosisResponse;
  }

  public static ReadDiagnosisResponseDTO create(DiagnosisWithQuestion diagnosisWithQuestion) {
    ReadDiagnosisResponseDTO readDiagnosisResponse = new ReadDiagnosisResponseDTO();
    readDiagnosisResponse.setId(diagnosisWithQuestion.getId());
    readDiagnosisResponse.setName(diagnosisWithQuestion.getName());
    readDiagnosisResponse.setNumberOfChoice(diagnosisWithQuestion.getNumberOfChoice());
    readDiagnosisResponse.addAllQuestions(diagnosisWithQuestion.getQuestions());
    readDiagnosisResponse.addAllQuestionBaseLines(diagnosisWithQuestion.getQuestionBaseLines());
    return readDiagnosisResponse;
  }

  private void addAllQuestions(List<Question> questionList) {
    questions.addAll(questionList);
  }

  private void addAllQuestionBaseLines(List<QuestionBaseLine> questionBaseLineList) {
    questionBaseLines.addAll(questionBaseLineList);
  }

}
