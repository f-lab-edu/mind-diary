package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.repository.DiagnosisRepository;
import com.mindDiary.mindDiary.strategy.ScoreCalculateStrategy;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

  private final DiagnosisRepository diagnosisRepository;
  private final QuestionBaseLineService questionBaseLineService;
  private final DiagnosisScoreService diagnosisScoreService;
  private final UserDiagnosisService userDiagnosisService;

  @Override
  public List<Diagnosis> readDiagnosis() {
    return diagnosisRepository.findDiagnosis();
  }

  @Override
  public DiagnosisWithQuestion readDiagnosisQuestions(int diagnosisId) {
    return diagnosisRepository.findDiagnosisWithQuestionById(diagnosisId);
  }


  @Override
  public UserDiagnosis createDiagnosisResult(int diagnosisId,
      List<Answer> answers, int userId) {

    List<QuestionBaseLine> questionBaseLines = questionBaseLineService.readByDiagnosisId(diagnosisId);
    int score = calc(questionBaseLines, answers);

    DiagnosisScore diagnosisScore = diagnosisScoreService.readOneByDiagnosisIdAndScore(diagnosisId, score);

    UserDiagnosis userDiagnosis = UserDiagnosis
        .create(userId, diagnosisId, LocalDateTime.now(), score, diagnosisScore.getContent());
    userDiagnosisService.save(userDiagnosis);

    return userDiagnosis;
  }
  public int calc(List<QuestionBaseLine> questionBaseLines, List<Answer> answers) {
    ScoreCalculateStrategy calculateStrategy = ScoreCalculateStrategy.createScores(questionBaseLines);
    return answers.stream()
        .mapToInt(a -> calculateStrategy.calc(a.getChoiceNumber(), a.getReverse()))
        .sum();
  }
}
