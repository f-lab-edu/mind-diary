package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadDiagnosisResultResponseDTO {

  private int diagnosisId;
  private int score;
  private LocalDateTime createdAt;
  private String content;

  public static ReadDiagnosisResultResponseDTO create(UserDiagnosis userDiagnosis) {

    return new ReadDiagnosisResultResponseDTO(
        userDiagnosis.getDiagnosisId(),
        userDiagnosis.getScore(),
        userDiagnosis.getCreatedAt(),
        userDiagnosis.getContent());
  }
}
