package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {

  private int id;
  private String content;
  private Reverse reverse;

  public enum Reverse {
    TRUE, FALSE
  }

}
