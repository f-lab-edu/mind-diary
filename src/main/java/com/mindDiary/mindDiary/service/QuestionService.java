package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Question;
import java.util.List;

public interface QuestionService {

  List<Question> findAllByDiagnosisIdInCache(int diagnosisId);

  List<Question> findAllByDiagnosisIdsInDB(List<Integer> diagnosisIds);

  List<Question> findAllByDiagnosisIdsInCache(List<Integer> diagnosisIds);

  void saveAllInCache(List<Question> questionList);

  void saveAllInDB(List<Question> questions);
}
