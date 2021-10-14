package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.repository.QuestionBaseLineRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionBaseLineServiceImpl implements QuestionBaseLineService {

  private final QuestionBaseLineRepository questionBaseLineRepository;

  @Override
  public List<QuestionBaseLine> readByDiagnosisId(int diagnosisId) {
    return questionBaseLineRepository.findByDiagnosisId(diagnosisId);
  }

  @Override
  public List<QuestionBaseLine> findAllByDiagnosisIds(List<Integer> diagnosisIds) {
    return questionBaseLineRepository.findAllByDiagnosisIds(diagnosisIds);
  }
}
