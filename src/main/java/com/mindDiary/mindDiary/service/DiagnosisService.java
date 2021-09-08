package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.request.CreateDiagnosisResultRequestDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResultResponseDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResponseDTO;
import java.util.List;

public interface DiagnosisService {

  List<ReadDiagnosisResponseDTO> readDiagnosis();

  ReadDiagnosisResponseDTO readDiagnosisQuestions(int diagnosisId);

  ReadDiagnosisResultResponseDTO createDiagnosisResult(int diagnosisId, CreateDiagnosisResultRequestDTO createDiagnosisResultRequest, int userId);

  List<ReadDiagnosisResultResponseDTO> readMyDiagnosisResults(int userId);
}
