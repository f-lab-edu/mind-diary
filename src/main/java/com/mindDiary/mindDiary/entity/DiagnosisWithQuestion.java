package com.mindDiary.mindDiary.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisWithQuestion extends Diagnosis {
  private List<Question> questions;
  private List<QuestionBaseLine> questionBaseLines;
}
