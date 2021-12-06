package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dao.DiagnosisDAO;
import com.mindDiary.mindDiary.dto.request.CreateDiagnosisRequestDTO;
import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.PageCriteria;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.exception.businessException.DiagnosisDuplicatedException;
import com.mindDiary.mindDiary.exception.businessException.InvalidScoreException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundDiagnosisException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundDiagnosisScoreException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundQuestionBaseLineException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundQuestionException;
import com.mindDiary.mindDiary.mapper.DiagnosisRepository;
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
  private final DiagnosisDAO diagnosisDAO;


  @Override
  public void saveAll() {
    List<Diagnosis> diagnoses = diagnosisRepository.findAll();
    if (diagnoses == null || diagnoses.isEmpty()) {
      throw new NotFoundDiagnosisException();
    }
    List<Integer> diagnosisIds = toDiagnosisIds(diagnoses);
    List<Question> questions = questionService.findAllByDiagnosisIdsInDB(diagnosisIds);

    if (questions == null || questions.isEmpty()) {
      throw new NotFoundQuestionException();
    }

    List<QuestionBaseLine> questionBaseLines = questionBaseLineService.findAllByDiagnosisIdsInDB(diagnosisIds);
    if(questionBaseLines == null || questionBaseLines.isEmpty()) {
      throw new NotFoundQuestionBaseLineException();
    }
    diagnosisDAO.saveAll(diagnoses);

    questionService.saveAllInCache(questions);
    questionBaseLineService.saveAllInCache(questionBaseLines);
  }

  @Override
  public Diagnosis readOne(int diagnosisId) {

    Diagnosis diagnosis = diagnosisDAO.findById(diagnosisId);
    if (isEmpty(diagnosis)) {
      diagnosis = diagnosisRepository.findById(diagnosisId);
      diagnosisDAO.save(diagnosis);
    }

    List<Question> questions = questionService.findAllByDiagnosisIdInCache(diagnosisId);
    if (questions.isEmpty()) {
      questions = questionService.findAllByDiagnosisIdInDB(diagnosisId);
      questionService.saveAllInCache(questions);
    }

    List<QuestionBaseLine> questionBaseLines =
        questionBaseLineService.readByDiagnosisIdInCache(diagnosisId);

    if (questionBaseLines.isEmpty()) {
      questionBaseLines = questionBaseLineService.readByDiagnosisIdInDB(diagnosisId);
      questionBaseLineService.saveAllInCache(questionBaseLines);
    }
    return diagnosis.withQuestionAndBaseLine(questions, questionBaseLines);
  }

  private boolean isEmpty(Diagnosis diagnosis) {
    return diagnosis.getId() == 0
        && diagnosis.getNumberOfChoice() == 0
        && diagnosis.getName().equals("")
        && diagnosis.getQuestions().isEmpty()
        && diagnosis.getQuestionBaseLines().isEmpty();
  }


  @Override
  public UserDiagnosis createDiagnosisResult(int diagnosisId,
      List<Answer> answers, int userId) {

    int score = calc(answers, diagnosisId);

    DiagnosisScore diagnosisScore = diagnosisScoreService.readOneByDiagnosisIdAndScore(diagnosisId, score);

    if (diagnosisScore == null) {
      throw new NotFoundDiagnosisScoreException();
    }
    UserDiagnosis userDiagnosis = UserDiagnosis
        .create(userId, diagnosisId, score,LocalDateTime.now(), diagnosisScore.getContent());

    userDiagnosisService.save(userDiagnosis);

    return userDiagnosis;
  }

  @Override
  public void create(CreateDiagnosisRequestDTO request) {

    Diagnosis diagnosis = request.createDiagnosisEntity();

    if (diagnosisRepository.findByName(diagnosis.getName()) != null) {
      throw new DiagnosisDuplicatedException();
    }

    diagnosisRepository.save(diagnosis);
    diagnosisScoreService.saveAllInDB(request.createDiagnosisScoreEntityList(diagnosis.getId()));
    questionService.saveAllInDB(request.createQuestionEntityList(diagnosis.getId()));
    questionBaseLineService.saveAllInDB(request.createQuestionBaseLineEntityList(diagnosis.getId()));

  }

  public List<Integer> createIntegerBaseLine(List<QuestionBaseLine> questionBaseLines) {
    return questionBaseLines.stream()
        .map(qb -> qb.getScore())
        .collect(Collectors.toList());
  }

  private List<Integer> toDiagnosisIds(List<Diagnosis> diagnoses) {
    return diagnoses.stream()
        .map(d -> d.getId())
        .collect(Collectors.toList());
  }

  public int calc(List<Answer> answers, int diagnosisId) {

    List<QuestionBaseLine> questionBaseLines = questionBaseLineService.readByDiagnosisIdInCache(diagnosisId);
    if (questionBaseLines.isEmpty()) {
      questionBaseLines = questionBaseLineService.readByDiagnosisIdInDB(diagnosisId);
      questionBaseLineService.saveAllInCache(questionBaseLines);
    }

    int score = scoreCalculateStrategy.calc(questionBaseLines, answers);

    if (score < 0) {
      throw new InvalidScoreException();
    }
    return score;
  }


  @Override
  public List<Diagnosis> readAll(int pageNumber) {
    PageCriteria pageCriteria = new PageCriteria(pageNumber);

    List<Diagnosis> diagnoses = diagnosisDAO.findAll(pageCriteria);

    if (diagnoses.isEmpty() || diagnoses.size() < PageCriteria.ITEMS_PER_PAGE) {
      diagnoses = diagnosisRepository.findAllWithPaging(pageCriteria);
      diagnosisDAO.saveAll(diagnoses);
    }

    return readAllWithQuestionAndQuestionBaseLine(diagnoses);
  }


  public List<Diagnosis> readAllWithQuestionAndQuestionBaseLine(List<Diagnosis> diagnoses) {
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

    List<Question> questions = questionService
        .findAllByDiagnosisIdsInCache(diagnosisIds);

    if (questions.isEmpty() || questions.size() < PageCriteria.ITEMS_PER_PAGE) {
      questions = questionService.findAllByDiagnosisIdsInDB(diagnosisIds);
      questionService.saveAllInCache(questions);
    }

    return questions
        .stream()
        .collect(Collectors.groupingBy(Question::getDiagnosisId));
  }

  private Map<Integer, List<QuestionBaseLine>> findQuestionBaseLinesMap(List<Integer> diagnosisIds) {

    List<QuestionBaseLine> questionBaseLines = questionBaseLineService
        .findAllByDiagnosisIdsInCache(diagnosisIds);

    if (questionBaseLines.isEmpty() || questionBaseLines.size() < PageCriteria.ITEMS_PER_PAGE) {
      questionBaseLines = questionBaseLineService.findAllByDiagnosisIdsInDB(diagnosisIds);
      questionBaseLineService.saveAllInCache(questionBaseLines);
    }

    return questionBaseLines
        .stream()
        .collect(Collectors.groupingBy(QuestionBaseLine::getDiagnosisId));
  }


}