package com.mindDiary.mindDiary.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diagnosis {

  private int id;
  private String name;
  private int numberOfChoice;
  private List<Question> questions;
  private List<QuestionBaseLine> questionBaseLines;

  public Diagnosis withQuestionAndBaseLine(List<Question> questionList, List<QuestionBaseLine> baseLineList) {
    return Diagnosis.builder()
        .id(id)
        .name(name)
        .numberOfChoice(numberOfChoice)
        .questions(questionList)
        .questionBaseLines(baseLineList)
        .build();
  }

  public boolean isEmpty() {
    return id == 0
        && numberOfChoice == 0
        && name.equals("")
        && questions.isEmpty()
        && questionBaseLines.isEmpty();
  }
}
