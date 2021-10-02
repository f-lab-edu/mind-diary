package com.mindDiary.mindDiary.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@Getter
@NoArgsConstructor
public class Diagnosis {

  private int id;
  private String name;
  private int numberOfChoice;
  private List<Question> questions = new ArrayList<>();
  private List<QuestionBaseLine> questionBaseLines = new ArrayList<>();

  public Diagnosis(int id, String name, int numberOfChoice) {
    this.id = id;
    this.name = name;
    this.numberOfChoice = numberOfChoice;
  }

  public Diagnosis(int id, String name, int numberOfChoice, List<Question> questions, List<QuestionBaseLine> questionBaseLines) {
    this.id = id;
    this.name = name;
    this.numberOfChoice = numberOfChoice;
    this.questions.addAll(questions);
    this.questionBaseLines.addAll(questionBaseLines);
  }
}
