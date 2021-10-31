package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import java.util.List;

public interface DiagnosisScoreService {

  DiagnosisScore readOneByDiagnosisIdAndScore(int diagnosisId, int score);

  void saveAllInDB(List<DiagnosisScore> diagnosisScores);
}
