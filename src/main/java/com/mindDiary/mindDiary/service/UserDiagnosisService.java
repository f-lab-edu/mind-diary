package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResultResponseDTO;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.util.List;

public interface UserDiagnosisService {

  void save(UserDiagnosis userDiagnosis);

  List<ReadDiagnosisResultResponseDTO> readMyDiagnosisResults(int userId);
}
