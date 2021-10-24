package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.Question;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepository {

  List<Question> findAllByDiagnosisIds(List<Integer> diagnosisIds);

  void saveAll(List<Question> questionList);
}
