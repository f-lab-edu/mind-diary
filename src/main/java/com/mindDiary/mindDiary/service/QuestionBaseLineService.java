package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;

public interface QuestionBaseLineService {

  List<QuestionBaseLine> readByDiagnosisIdInCache(int diagnosisId);

  List<QuestionBaseLine> findAllByDiagnosisIdsInDB(List<Integer> diagnosisIds);

  List<QuestionBaseLine> findAllByDiagnosisIdsInCache(List<Integer> diagnosisIds);

  void saveAllInCache(List<QuestionBaseLine> questionBaseLines);

  void saveAllInDB(List<QuestionBaseLine> questionBaseLines);

  List<QuestionBaseLine> readByDiagnosisIdInDB(int diagnosisId);
}
