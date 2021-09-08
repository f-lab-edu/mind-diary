package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Question;
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
  private final QuestionService questionService;
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
    List<Question> questions = questionService.readByAnswers(answers);

    ScoreCalculateStrategy calculateStrategy = ScoreCalculateStrategy.createScores(questionBaseLines);

    int score = questions.stream()
        .mapToInt(q -> calculateStrategy.calc(q.getChoiceNumber(), q.getReverse()))
        .sum();

    DiagnosisScore diagnosisScore = diagnosisScoreService.readOneByDiagnosisIdAndScore(diagnosisId, score);
    String content = diagnosisScore.getContent();

    UserDiagnosis userDiagnosis = UserDiagnosis
        .create(userId, diagnosisId, LocalDateTime.now(), score, content);
    userDiagnosisService.save(userDiagnosis);

    return userDiagnosis;
  }
}
