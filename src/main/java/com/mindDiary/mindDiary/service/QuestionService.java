package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Question;
import java.util.List;

public interface QuestionService {

  List<Question> findByDiagnosisId(int diagnosisId);

  List<Question> findAllByDiagnosisIdsInDB(List<Integer> diagnosisIds);

  List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds);

  void saveAll(List<Question> questionList);

  void saveAllInDB(List<Question> questions);
}
