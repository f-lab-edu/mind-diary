package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.util.List;

public interface DiagnosisService {

  List<Diagnosis> readDiagnoses();

  DiagnosisWithQuestion readDiagnosisWithQuestions(int diagnosisId);

  UserDiagnosis createDiagnosisResult(int diagnosisId, List<Answer> answers, int userId);

}
