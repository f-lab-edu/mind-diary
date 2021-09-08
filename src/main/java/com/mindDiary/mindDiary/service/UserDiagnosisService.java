package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.util.List;

public interface UserDiagnosisService {

  void save(UserDiagnosis userDiagnosis);

  List<UserDiagnosis> readMyDiagnosisResults(int userId);
}
