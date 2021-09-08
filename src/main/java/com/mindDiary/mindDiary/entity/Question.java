package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {

  private int id;
  private String content;
  private Reverse reverse;
  private int choiceNumber;

  public static Question create(int id, int choiceNumber) {
    Question question = new Question();
    question.setId(id);
    question.setChoiceNumber(choiceNumber);
    return question;
  }

  public enum Reverse {
    TRUE, FALSE
  }

}
