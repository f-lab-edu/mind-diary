package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.util.List;

public interface DiagnosisService {

  void saveAll();

  List<Diagnosis> readAll(int pageNumber);

  Diagnosis readOne(int diagnosisId);

  UserDiagnosis createDiagnosisResult(int diagnosisId, List<Answer> answers, int userId);

}
