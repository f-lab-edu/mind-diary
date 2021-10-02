package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBaseLineDTO {
  private int id;
  private String content;
  private int score;

  public static QuestionBaseLineDTO create(QuestionBaseLine questionBaseLine) {
    return new QuestionBaseLineDTO(
        questionBaseLine.getId(),
        questionBaseLine.getContent(),
        questionBaseLine.getScore());
  }
}
