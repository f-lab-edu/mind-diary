package com.mindDiary.mindDiary.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDiagnosisResultRequestDTO {

  @NotNull
  List<QuestionAnswerRequestDTO> questionAnswerRequests;
}
