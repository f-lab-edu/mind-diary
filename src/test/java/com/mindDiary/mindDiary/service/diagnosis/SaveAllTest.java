package com.mindDiary.mindDiary.service.diagnosis;

import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dao.DiagnosisDAO;
import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.exception.businessException.NotFoundDiagnosisException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundQuestionBaseLineException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundQuestionException;
import com.mindDiary.mindDiary.mapper.DiagnosisRepository;
import com.mindDiary.mindDiary.service.DiagnosisScoreService;
import com.mindDiary.mindDiary.service.DiagnosisServiceImpl;
import com.mindDiary.mindDiary.service.QuestionBaseLineService;
import com.mindDiary.mindDiary.service.QuestionService;
import com.mindDiary.mindDiary.service.UserDiagnosisService;
import com.mindDiary.mindDiary.strategy.Calculator.ScoreCalculator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SaveAllTest {

  @InjectMocks
  DiagnosisServiceImpl diagnosisService;

  @Mock
  DiagnosisRepository diagnosisRepository;

  @Mock
  QuestionBaseLineService questionBaseLineService;

  @Mock
  DiagnosisScoreService diagnosisScoreService;

  @Mock
  UserDiagnosisService userDiagnosisService;

  @Mock
  ScoreCalculator scoreCalculator;

  @Mock
  QuestionService questionService;

  @Mock
  DiagnosisDAO diagnosisDAO;


  @Test
  @DisplayName("자가진단, 질문지, 점수지 데이터를 캐시에 저장 성공")
  void saveAllSuccess1() {
    // Arrange
    List<Diagnosis> diagnoses = new ArrayList<>();
    diagnoses.add(makeDiagnosis());
    List<Question> questions = makeQuestionList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();

    doReturn(diagnoses)
        .when(diagnosisRepository)
        .findAll();

    doReturn(questions)
        .when(questionService)
        .findAllByDiagnosisIdsInDB(argThat(diagnosisIds -> IntStream.range(0, diagnosisIds.size())
            .allMatch(i -> diagnosisIds.get(i) == diagnoses.get(i).getId())));


    doReturn(baseLines)
        .when(questionBaseLineService)
        .findAllByDiagnosisIdsInDB(argThat(diagnosisIds -> IntStream.range(0, diagnosisIds.size())
            .allMatch(i -> diagnosisIds.get(i) == diagnoses.get(i).getId())));


    // Act
    diagnosisService.saveAll();

    // Assert
    verify(diagnosisDAO, times(1))
        .saveAll(argThat(d -> IntStream.range(0, d.size())
            .allMatch(i -> d.get(i).getName().equals(diagnoses.get(i).getName())
                && d.get(i).getNumberOfChoice() == diagnoses.get(i).getNumberOfChoice())));

    verify(questionService, times(1))
        .saveAllInCache(argThat(q ->
            IntStream.range(0, q.size())
                .allMatch(i -> q.get(i).getReverse().equals(questions.get(i).getReverse())
                    && q.get(i).getContent().equals(questions.get(i).getContent())
                )));

    verify(questionBaseLineService, times(1))
        .saveAllInCache(argThat(b ->
            IntStream.range(0, b.size())
                .allMatch(i -> b.get(i).getScore() == baseLines.get(i).getScore()
                    && b.get(i).getContent().equals(baseLines.get(i).getContent())
                )));

  }


  @Test
  @DisplayName("자가진단, 질문지, 점수지 데이터를 캐시에 저장 실패 : "
      + "자가진단 데이터가 DB에 존재하지 않으면 어떤 것도 캐시에 저장할 수 없음")
  void saveAllFail1() {

    // Arrange
    List<Diagnosis> diagnoses = new ArrayList<>();
    diagnoses.add(makeDiagnosis());
    List<Question> questions = makeQuestionList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();

    doReturn(null)
        .when(diagnosisRepository)
        .findAll();

    // Act
    assertThatThrownBy(() -> {
      diagnosisService.saveAll();
    }).isInstanceOf(NotFoundDiagnosisException.class);

    // Assert

    verify(diagnosisDAO, times(0))
        .saveAll(anyList());

    verify(questionService, times(0))
        .saveAllInCache(anyList());

    verify(questionBaseLineService, times(0))
        .saveAllInCache(anyList());
  }


  @Test
  @DisplayName("자가진단, 질문지, 점수지 데이터를 캐시에 저장 실패 : "
      + "질문지 데이터가 DB에 존재하지 않으면 어떤 것도 캐시에 저장할 수 없음")
  void saveAllFail2() {

    // Arrange
    List<Diagnosis> diagnoses = new ArrayList<>();
    diagnoses.add(makeDiagnosis());
    List<Question> questions = makeQuestionList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();

    doReturn(diagnoses)
        .when(diagnosisRepository)
        .findAll();

    doReturn(null)
        .when(questionService)
        .findAllByDiagnosisIdsInDB(argThat(diagnosisIds ->
            IntStream.range(0, diagnosisIds.size())
        .allMatch(i -> diagnosisIds.get(i) == diagnoses.get(i).getId())));

    // Act
    assertThatThrownBy(() -> {
      diagnosisService.saveAll();
    }).isInstanceOf(NotFoundQuestionException.class);

    // Assert
    verify(diagnosisDAO, times(0))
        .saveAll(anyList());

    verify(questionService, times(0))
        .saveAllInCache(anyList());

    verify(questionBaseLineService, times(0))
        .saveAllInCache(anyList());

  }

  @Test
  @DisplayName("자가진단, 질문지, 점수지 데이터를 캐시에 저장 실패 : "
      + "점수지 데이터가 DB에 존재하지 않으면 어떤 것도 캐시에 저장할 수 없음")
  void saveAllFail3() {
    // Arrange
    List<Diagnosis> diagnoses = new ArrayList<>();
    diagnoses.add(makeDiagnosis());
    List<Question> questions = makeQuestionList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();

    doReturn(diagnoses)
        .when(diagnosisRepository)
        .findAll();

    doReturn(questions)
        .when(questionService)
        .findAllByDiagnosisIdsInDB(argThat(diagnosisIds ->
            IntStream.range(0, diagnosisIds.size())
                .allMatch(i -> diagnosisIds.get(i) == diagnoses.get(i).getId())));

    doReturn(null)
        .when(questionBaseLineService)
        .findAllByDiagnosisIdsInDB(argThat(diagnosisIds ->
            IntStream.range(0, diagnosisIds.size())
                .allMatch(i -> diagnosisIds.get(i) == diagnoses.get(i).getId())));

    // Act
    assertThatThrownBy(() -> {
      diagnosisService.saveAll();
    }).isInstanceOf(NotFoundQuestionBaseLineException.class);

    // Assert
    verify(diagnosisDAO, times(0))
        .saveAll(anyList());

    verify(questionService, times(0))
        .saveAllInCache(anyList());

    verify(questionBaseLineService, times(0))
        .saveAllInCache(anyList());

  }
}
