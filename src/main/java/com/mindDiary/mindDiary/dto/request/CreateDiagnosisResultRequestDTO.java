package com.mindDiary.mindDiary.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiagnosisResultRequestDTO {

  @NotNull
  List<QuestionAnswerRequestDTO> questionAnswerRequests;
}
