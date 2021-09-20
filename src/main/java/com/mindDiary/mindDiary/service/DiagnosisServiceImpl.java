package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.repository.DiagnosisRepository;
import com.mindDiary.mindDiary.strategy.scoreCalc.ScoreCalculateStrategy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
  private final ScoreCalculateStrategy scoreCalculateStrategy;

  @Override
  public List<Diagnosis> readDiagnoses() {
    return diagnosisRepository.findDiagnoses();
  }

  @Override
  public DiagnosisWithQuestion readDiagnosisWithQuestions(int diagnosisId) {
    return diagnosisRepository.findDiagnosisWithQuestionById(diagnosisId);
  }


  @Override
  public UserDiagnosis createDiagnosisResult(int diagnosisId,
      List<Answer> answers, int userId) {

    int score = calc(answers, diagnosisId);

    DiagnosisScore diagnosisScore = diagnosisScoreService.readOneByDiagnosisIdAndScore(diagnosisId, score);

    UserDiagnosis userDiagnosis = UserDiagnosis
        .create(userId, diagnosisId, score, LocalDateTime.now(), diagnosisScore.getContent());

    userDiagnosisService.save(userDiagnosis);

    return userDiagnosis;
  }

  public List<Integer> createIntegerBaseLine(List<QuestionBaseLine> questionBaseLines) {
    return questionBaseLines.stream()
        .map(qb -> qb.getScore())
        .collect(Collectors.toList());
  }

  public int calc(List<Answer> answers, int diagnosisId) {

    List<QuestionBaseLine> questionBaseLines = questionBaseLineService.readByDiagnosisId(diagnosisId);
    scoreCalculateStrategy.addScoreBaseLine(createIntegerBaseLine(questionBaseLines));

    return answers.stream()
        .mapToInt(a -> scoreCalculateStrategy.calc(a.getChoiceNumber(), a.getReverse()))
        .sum();
  }

}