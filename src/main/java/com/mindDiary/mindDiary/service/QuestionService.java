package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Question;
import java.util.List;

public interface QuestionService {

  List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds);
}
