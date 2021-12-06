package com.mindDiary.mindDiary.service.diagnosis;


import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import com.mindDiary.mindDiary.dao.DiagnosisDAO;
import com.mindDiary.mindDiary.dto.request.CreateDiagnosisRequestDTO;

import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.exception.businessException.DiagnosisDuplicatedException;
import com.mindDiary.mindDiary.mapper.DiagnosisRepository;
import com.mindDiary.mindDiary.service.DiagnosisScoreService;
import com.mindDiary.mindDiary.service.DiagnosisServiceImpl;
import com.mindDiary.mindDiary.service.QuestionBaseLineService;
import com.mindDiary.mindDiary.service.QuestionService;
import com.mindDiary.mindDiary.service.UserDiagnosisService;
import com.mindDiary.mindDiary.strategy.scoreCalc.ScoreCalculateStrategy;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateTest {

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
  ScoreCalculateStrategy scoreCalculateStrategy;

  @Mock
  QuestionService questionService;

  @Mock
  DiagnosisDAO diagnosisDAO;

  @Test
  @DisplayName("자가진단 , 질문지, 점수지, 기준표 데이터를 DB에 저장 성공")
  void createSuccess1() {
    // Arrange
    CreateDiagnosisRequestDTO request
        = makeCreateDiagnosisRequestDTO();

    Diagnosis diagnosis = makeDiagnosis(request);
    List<Question> questions = diagnosis.getQuestions();
    List<QuestionBaseLine> baseLines = diagnosis.getQuestionBaseLines();
    List<DiagnosisScore> scores = makeScoreList(request.getDiagnosisScores());

    doReturn(null)
        .when(diagnosisRepository)
        .findByName(argThat(n -> n.equals(request.getName())));
    // Act
    diagnosisService.create(request);

    // Assert
    verify(diagnosisRepository, times(1))
        .save(argThat(d -> d.getName().equals(request.getName()) &&
            d.getNumberOfChoice() == request.getNumberOfChoice()
        ));

    verify(diagnosisScoreService, times(1))
        .saveAllInDB(argThat(ds ->
            IntStream.range(0, ds.size())
                .allMatch(i -> ds.get(i).getContent().equals(scores.get(i).getContent())
                    && ds.get(i).getMaxValue() == scores.get(i).getMaxValue()
                    && ds.get(i).getMinValue() == scores.get(i).getMinValue())
        ));

    verify(questionService, times(1))
        .saveAllInDB(argThat(q ->
            IntStream.range(0, q.size())
                .allMatch(i -> q.get(i).getReverse().equals(questions.get(i).getReverse())
                    && q.get(i).getContent().equals(questions.get(i).getContent())
                )));

    verify(questionBaseLineService, times(1))
        .saveAllInDB(argThat(b ->
            IntStream.range(0, b.size())
                .allMatch(i -> b.get(i).getScore() == baseLines.get(i).getScore()
                    && b.get(i).getContent().equals(baseLines.get(i).getContent())
                )));

  }

  @Test
  @DisplayName("자가진단 , 질문지, 점수지, 기준표 데이터를 DB에 저장 실패 : "
      + "자가진단 데이터가 이미 존재하는 경우")
  void createFail1() {
    // Arrange
    CreateDiagnosisRequestDTO request
        = makeCreateDiagnosisRequestDTO();

    Diagnosis diagnosis = makeDiagnosis(request);
    List<Question> questions = diagnosis.getQuestions();
    List<QuestionBaseLine> baseLines = diagnosis.getQuestionBaseLines();
    List<DiagnosisScore> scores = makeScoreList(request.getDiagnosisScores());

    doReturn(diagnosis)
        .when(diagnosisRepository)
        .findByName(argThat(n -> n.equals(request.getName())));
    // Act
    assertThatThrownBy(() -> {
      diagnosisService.create(request);
    }).isInstanceOf(DiagnosisDuplicatedException.class);

    // Assert
    verify(diagnosisRepository, times(0))
        .save(argThat(d -> d.getName().equals(request.getName()) &&
            d.getNumberOfChoice() == request.getNumberOfChoice()
        ));

    verify(diagnosisScoreService, times(0))
        .saveAllInDB(argThat(ds ->
            IntStream.range(0, ds.size())
                .allMatch(i -> ds.get(i).getContent().equals(scores.get(i).getContent())
                    && ds.get(i).getMaxValue() == scores.get(i).getMaxValue()
                    && ds.get(i).getMinValue() == scores.get(i).getMinValue())
        ));

    verify(questionService, times(0))
        .saveAllInDB(argThat(q ->
            IntStream.range(0, q.size())
                .allMatch(i -> q.get(i).getReverse().equals(questions.get(i).getReverse())
                    && q.get(i).getContent().equals(questions.get(i).getContent())
                )));

    verify(questionBaseLineService, times(0))
        .saveAllInDB(argThat(b ->
            IntStream.range(0, b.size())
                .allMatch(i -> b.get(i).getScore() == baseLines.get(i).getScore()
                    && b.get(i).getContent().equals(baseLines.get(i).getContent())
                )));
  }
}
