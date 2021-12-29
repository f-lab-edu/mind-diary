package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

  private int id;
  private int diagnosisId;
  private String content;
  private Reverse reverse;

}
