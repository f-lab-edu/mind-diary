package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.mapper.DiagnosisScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosisScoreServiceImpl implements DiagnosisScoreService {

  private final DiagnosisScoreRepository diagnosisScoreRepository;

  @Override
  public DiagnosisScore readOneByDiagnosisIdAndScore(int diagnosisId, int score) {
    return diagnosisScoreRepository.findByDiagnosisIdAndScore(diagnosisId, score);
  }
}
