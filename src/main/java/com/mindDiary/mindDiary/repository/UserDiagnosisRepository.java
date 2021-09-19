package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDiagnosisRepository {

  void save(UserDiagnosis userDiagnosis);

  List<UserDiagnosis> findByUserId(int userId);
}
