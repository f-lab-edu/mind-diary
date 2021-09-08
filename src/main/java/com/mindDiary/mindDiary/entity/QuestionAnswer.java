package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswer {

  private int questionId;
  private int choiceNumber;

  public static QuestionAnswer create(int questionId, int choiceNumber) {
    QuestionAnswer questionAnswer = new QuestionAnswer();
    questionAnswer.setQuestionId(questionId);
    questionAnswer.setChoiceNumber(choiceNumber);
    return questionAnswer;
  }
}
