package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.PageCriteria;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.repository.DiagnosisRepository;
import com.mindDiary.mindDiary.strategy.scoreCalc.ScoreCalculateStrategy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
  private final QuestionService questionService;

  @Override
  public List<Diagnosis> readAll(int pageNumber) {

    PageCriteria pageCriteria = new PageCriteria(pageNumber);
    List<Diagnosis> diagnoses = diagnosisRepository.findAll(pageCriteria);
    List<Integer> diagnosisIds = toDiagnosisIds(diagnoses);

    Map<Integer, List<Question>> questionMap
        = findQuestionsMap(diagnosisIds);

    Map<Integer, List<QuestionBaseLine>> questionBaseLineMap
        = findQuestionBaseLinesMap(diagnosisIds);

    return diagnoses.stream()
        .map(diagnosis-> diagnosis.withQuestionAndBaseLine(
            questionMap.get(diagnosis.getId()),
            questionBaseLineMap.get(diagnosis.getId())))
        .collect(Collectors.toList());
  }

  private Map<Integer, List<Question>> findQuestionsMap(List<Integer> diagnosisIds) {
    return questionService
        .findAllByDiagnosisIds(diagnosisIds)
        .stream()
        .collect(Collectors.groupingBy(Question::getDiagnosisId));
  }

  private Map<Integer, List<QuestionBaseLine>> findQuestionBaseLinesMap(List<Integer> diagnosisIds) {
    return questionBaseLineService
        .findAllByDiagnosisIds(diagnosisIds)
        .stream()
        .collect(Collectors.groupingBy(QuestionBaseLine::getDiagnosisId));
  }

  private List<Integer> toDiagnosisIds(List<Diagnosis> diagnoses) {
    return diagnoses.stream()
        .map(d -> d.getId())
        .collect(Collectors.toList());
  }


  @Override
  public Diagnosis readOne(int diagnosisId) {
    return diagnosisRepository.findById(diagnosisId);
  }


  @Override
  public UserDiagnosis createDiagnosisResult(int diagnosisId,
      List<Answer> answers, int userId) {

    int score = calc(answers, diagnosisId);

    DiagnosisScore diagnosisScore = diagnosisScoreService.readOneByDiagnosisIdAndScore(diagnosisId, score);

    UserDiagnosis userDiagnosis = UserDiagnosis
        .create(userId, diagnosisId, score,LocalDateTime.now(), diagnosisScore.getContent());

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