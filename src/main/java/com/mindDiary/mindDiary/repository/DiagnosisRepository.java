package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionAnswer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiagnosisRepository {

  List<Diagnosis> findDiagnosis();

  DiagnosisWithQuestion findDiagnosisWithQuestionById(int diagnosisId);

  List<QuestionBaseLine> findQuestionBaseLinesById(int diagnosisId);

  List<Question> findQuestionsByAnswers(List<QuestionAnswer> questionAnswer);

  DiagnosisScore findDiagnosisScore(int diagnosisId, int score);

  void save(UserDiagnosis userDiagnosis);

  List<UserDiagnosis> findUserDiagnosisById(int userId);
}
