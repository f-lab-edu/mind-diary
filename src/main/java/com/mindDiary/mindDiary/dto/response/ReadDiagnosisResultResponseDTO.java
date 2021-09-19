package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ReadDiagnosisResultResponseDTO {

  private int diagnosisId;
  private int score;
  private LocalDateTime createdAt;
  private String content;

  public static ReadDiagnosisResultResponseDTO create(UserDiagnosis userDiagnosis) {
    ReadDiagnosisResultResponseDTO diagnosisResultScoreResponse = new ReadDiagnosisResultResponseDTO();
    diagnosisResultScoreResponse.setScore(userDiagnosis.getScore());
    diagnosisResultScoreResponse.setCreatedAt(userDiagnosis.getCreatedAt());
    diagnosisResultScoreResponse.setContent(userDiagnosis.getContent());
    diagnosisResultScoreResponse.setDiagnosisId(userDiagnosis.getDiagnosisId());
    return diagnosisResultScoreResponse;
  }
}
