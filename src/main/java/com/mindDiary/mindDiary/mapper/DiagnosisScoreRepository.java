package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiagnosisScoreRepository {

  DiagnosisScore findByDiagnosisIdAndScore(int diagnosisId, int score);
}
