package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.DiagnosisScore;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiagnosisScoreRepository {

  DiagnosisScore findByDiagnosisIdAndScore(int diagnosisId, int score);

  void saveAll(List<DiagnosisScore> diagnosisScores);
}
