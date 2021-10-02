package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Question;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepository {

  List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds);
}
