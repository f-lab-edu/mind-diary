package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;

  @Override
  public List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds) {
    return questionRepository.findAllByDiagnosisIds(diagnosisIds);
  }
}
