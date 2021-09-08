package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;

public interface QuestionBaseLineService {

  List<QuestionBaseLine> readByDiagnosisId(int diagnosisId);
}
