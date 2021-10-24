package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;

public interface QuestionBaseLineService {

  List<QuestionBaseLine> readByDiagnosisId(int diagnosisId);

  List<QuestionBaseLine> findAllByDiagnosisIdsInDB(List<Integer> diagnosisIds);

  List<QuestionBaseLine> findAllByDiagnosisIds(List<Integer> diagnosisIds);

  void saveAll(List<QuestionBaseLine> questionBaseLines);

  void saveAllInDB(List<QuestionBaseLine> questionBaseLines);
}
