package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionBaseLineRepository {

  List<QuestionBaseLine> findByDiagnosisId(int diagnosisId);

  List<QuestionBaseLine> findAllByDiagnosisIds(List<Integer> diagnosisIds);
}
