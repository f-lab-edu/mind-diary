package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.DiagnosisScore;

public interface DiagnosisScoreService {

  DiagnosisScore readOneByDiagnosisIdAndScore(int diagnosisId, int score);
}
