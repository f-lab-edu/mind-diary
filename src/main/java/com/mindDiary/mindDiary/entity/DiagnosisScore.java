package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisScore {
  private int id;
  private int minValue;
  private int maxValue;
  private String content;
}
