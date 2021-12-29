package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

  private int questionId;
  private int choiceNumber;
  private Reverse reverse;
}
