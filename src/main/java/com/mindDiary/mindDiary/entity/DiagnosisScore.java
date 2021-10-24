package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisScore {

  private int id;
  private int diagnosisId;
  private int minValue;
  private int maxValue;
  private String content;
}
