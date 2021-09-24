package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadDiagnosisResponseDTO {

  private int id;
  private String name;
  private int numberOfChoice;
  private List<QuestionBaseLine> questionBaseLines;
  private List<Question> questions;

  public ReadDiagnosisResponseDTO(int id, String name, int numberOfChoice) {
    this.id = id;
    this.name = name;
    this.numberOfChoice = numberOfChoice;
    this.questionBaseLines = new ArrayList<>();
    this.questions = new ArrayList<>();
  }

  public ReadDiagnosisResponseDTO(int id, String name, int numberOfChoice,
      List<Question> questions,
      List<QuestionBaseLine> questionBaseLines) {
    this.id = id;
    this.name = name;
    this.numberOfChoice = numberOfChoice;
//    this.questions.addAll(questions);
    //this.questionBaseLines.addAll(questionBaseLines);
    this.questions = new ArrayList<>(questions);
    this.questionBaseLines = new ArrayList<>(questionBaseLines);
  }

  public static ReadDiagnosisResponseDTO create(Diagnosis diagnosis) {
    /*
    ReadDiagnosisResponseDTO readDiagnosisResponse = new ReadDiagnosisResponseDTO();
    readDiagnosisResponse.setId(diagnosis.getId());
    readDiagnosisResponse.setName(diagnosis.getName());
    readDiagnosisResponse.setNumberOfChoice(diagnosis.getNumberOfChoice());
    */
    return new ReadDiagnosisResponseDTO(
        diagnosis.getId(),
        diagnosis.getName(),
        diagnosis.getNumberOfChoice());
  }

  public static ReadDiagnosisResponseDTO create(DiagnosisWithQuestion diagnosisWithQuestion) {
    /*
    ReadDiagnosisResponseDTO readDiagnosisResponse = new ReadDiagnosisResponseDTO();
    readDiagnosisResponse.setId(diagnosisWithQuestion.getId());
    readDiagnosisResponse.setName(diagnosisWithQuestion.getName());
    readDiagnosisResponse.setNumberOfChoice(diagnosisWithQuestion.getNumberOfChoice());
    readDiagnosisResponse.addAllQuestions(diagnosisWithQuestion.getQuestions());
    readDiagnosisResponse.addAllQuestionBaseLines(diagnosisWithQuestion.getQuestionBaseLines());
     */

    return new ReadDiagnosisResponseDTO(
        diagnosisWithQuestion.getId(),
        diagnosisWithQuestion.getName(),
        diagnosisWithQuestion.getNumberOfChoice(),
        diagnosisWithQuestion.getQuestions(),
        diagnosisWithQuestion.getQuestionBaseLines());
  }

  /*
  private void addAllQuestions(List<Question> questionList) {
    questions.addAll(questionList);
  }

  private void addAllQuestionBaseLines(List<QuestionBaseLine> questionBaseLineList) {
    questionBaseLines.addAll(questionBaseLineList);
  }
*/
}
