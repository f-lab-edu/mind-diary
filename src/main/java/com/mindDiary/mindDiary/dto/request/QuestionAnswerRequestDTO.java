package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Reverse;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerRequestDTO {

  @NotNull
  private int questionId;
  @NotNull
  private int choiceNumber;

  @NotNull
  private Reverse reverse;
}
