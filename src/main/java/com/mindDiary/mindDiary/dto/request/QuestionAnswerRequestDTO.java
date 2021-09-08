package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Reverse;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class QuestionAnswerRequestDTO {
  @NotNull
  private int questionId;
  @NotNull
  private int choiceNumber;

  @NotNull
  private Reverse reverse;
}
