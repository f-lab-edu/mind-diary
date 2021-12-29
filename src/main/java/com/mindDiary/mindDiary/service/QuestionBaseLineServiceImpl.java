package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dao.QuestionBaseLineDAO;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.mapper.QuestionBaseLineRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionBaseLineServiceImpl implements QuestionBaseLineService {

  private final QuestionBaseLineRepository questionBaseLineRepository;
  private final QuestionBaseLineDAO questionBaseLineDAO;

  @Override
  public List<QuestionBaseLine> readByDiagnosisIdInCache(int diagnosisId) {
    return questionBaseLineDAO.findByDiagnosisId(diagnosisId);
  }

  @Override
  public List<QuestionBaseLine> readByDiagnosisIdInDB(int diagnosisId) {
    return questionBaseLineRepository.findByDiagnosisId(diagnosisId);
  }

  @Override
  public List<QuestionBaseLine> findAllByDiagnosisIdsInDB(List<Integer> diagnosisIds) {
    return questionBaseLineRepository.findAllByDiagnosisIds(diagnosisIds);
  }

  @Override
  public List<QuestionBaseLine> findAllByDiagnosisIdsInCache(List<Integer> diagnosisIds) {
    return questionBaseLineDAO.findAllByDiagnosisIds(diagnosisIds);
  }

  @Override
  public void saveAllInCache(List<QuestionBaseLine> questionBaseLines) {
    questionBaseLineDAO.saveAll(questionBaseLines);
  }

  @Override
  public void saveAllInDB(List<QuestionBaseLine> questionBaseLines) {
    questionBaseLineRepository.saveAll(questionBaseLines);
  }


}
