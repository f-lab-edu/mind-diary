package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.Reverse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

  private int id;
  private String content;
  private Reverse reverse;

  public static QuestionDTO create(Question question) {
    return new QuestionDTO(
        question.getId(),
        question.getContent(),
        question.getReverse());
  }
}
