package com.mindDiary.mindDiary.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisWithQuestion {

  private int id;
  private String name;
  private int numberOfChoice;
  private List<Question> questions;
  private List<QuestionBaseLine> questionBaseLines;

}
