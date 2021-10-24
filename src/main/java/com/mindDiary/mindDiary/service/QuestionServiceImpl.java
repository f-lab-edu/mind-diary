package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dao.QuestionDAO;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.mapper.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionDAO questionDAO;

  @Override
  public List<Question> findByDiagnosisId(int diagnosisId) {
    return questionDAO.findByDiagnosisId(diagnosisId);
  }

  @Override
  public List<Question> findAllByDiagnosisIdsInDB(List<Integer> diagnosisIds) {
    return questionRepository.findAllByDiagnosisIds(diagnosisIds);
  }

  @Override
  public List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds) {
    return questionDAO.findAllByDiagnosisIds(diagnosisIds);
  }

  @Override
  public void saveAll(List<Question> questions) {
    questionDAO.saveAll(questions);
  }

  @Override
  public void saveAllInDB(List<Question> questions) {
    questionRepository.saveAll(questions);
  }


}
