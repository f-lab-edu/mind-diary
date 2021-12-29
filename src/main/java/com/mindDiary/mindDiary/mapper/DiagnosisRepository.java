package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.PageCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiagnosisRepository {

  List<Diagnosis> findAllWithPaging(PageCriteria pageCriteria);

  Diagnosis findById(int diagnosisId);

  List<Diagnosis> findAll();

  void save(Diagnosis diagnosis);

  Diagnosis findByName(String name);
}
