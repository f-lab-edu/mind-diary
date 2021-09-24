package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiagnosisRepository {

  List<Diagnosis> findDiagnoses();

  DiagnosisWithQuestion findDiagnosisWithQuestionById(int diagnosisId);

}
