package com.mindDiary.mindDiary.service.diagnosis;

import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.dao.DiagnosisDAO;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.exception.businessException.InvalidScoreException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundDiagnosisScoreException;
import com.mindDiary.mindDiary.mapper.DiagnosisRepository;
import com.mindDiary.mindDiary.service.DiagnosisScoreService;
import com.mindDiary.mindDiary.service.DiagnosisServiceImpl;
import com.mindDiary.mindDiary.service.QuestionBaseLineService;
import com.mindDiary.mindDiary.service.QuestionService;
import com.mindDiary.mindDiary.service.UserDiagnosisService;
import com.mindDiary.mindDiary.strategy.Calculator.ScoreCalculator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateDiagnosisResultTest {

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
  QuestionService questionService;

  @Mock
  DiagnosisDAO diagnosisDAO;

  @Mock
  ScoreCalculator scoreCalculator;

  @Test
  @DisplayName("사용자의 자가진단 점수에 따라 진단 결과를 저장")
  void createDiagnosisResultSuccess1() {

    // Arrange
    List<Answer> answers = makeAnswerList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();
    DiagnosisScore diagnosisScore = makeDiagnosisScore();
    UserDiagnosis userDiagnosis
        = makeUserDiagnosis(diagnosisScore);

    int userId = userDiagnosis.getUserId();
    int diagnosisId = userDiagnosis.getDiagnosisId();
    int score = userDiagnosis.getScore();
    String content = userDiagnosis.getContent();

    doReturn(baseLines)
        .when(questionBaseLineService)
        .readByDiagnosisIdInCache(diagnosisId);

    doReturn(score)
        .when(scoreCalculator)
        .calc(baseLines, answers);

    doReturn(diagnosisScore)
        .when(diagnosisScoreService)
        .readOneByDiagnosisIdAndScore(diagnosisId, score);


    // Act
    UserDiagnosis result = diagnosisService
        .createDiagnosisResult(diagnosisId, answers, userId);

    // Assert

    verify(userDiagnosisService, times(1))
        .save(argThat(ud -> ud.getContent().equals(userDiagnosis.getContent())
        && ud.getScore() == userDiagnosis.getScore()
        && ud.getDiagnosisId() == userDiagnosis.getDiagnosisId()
        && ud.getUserId() == userDiagnosis.getUserId()));

    assertThat(result.getDiagnosisId())
        .isEqualTo(diagnosisId);
    assertThat(result.getContent())
        .isEqualTo(content);
    assertThat(result.getScore())
        .isEqualTo(score);
    assertThat(result.getUserId())
        .isEqualTo(userId);
  }


  @Test
  @DisplayName("자가진단 점수 결과를 DB에서 찾을 수 없어 진단 결과 DB 저장 실패")
  void createDiagnosisResultFail1() {

    // Arrange
    List<Answer> answers = makeAnswerList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();
    DiagnosisScore diagnosisScore = makeDiagnosisScore();
    UserDiagnosis userDiagnosis
        = makeUserDiagnosis(diagnosisScore);

    int userId = userDiagnosis.getUserId();
    int diagnosisId = userDiagnosis.getDiagnosisId();
    int score = userDiagnosis.getScore();
    String content = userDiagnosis.getContent();

    doReturn(baseLines)
        .when(questionBaseLineService)
        .readByDiagnosisIdInCache(diagnosisId);

    doReturn(score)
        .when(scoreCalculator)
        .calc(baseLines, answers);

    doReturn(null)
        .when(diagnosisScoreService)
        .readOneByDiagnosisIdAndScore(diagnosisId, score);

    // Act , Assert
    assertThatThrownBy(() -> {
      UserDiagnosis result = diagnosisService
          .createDiagnosisResult(diagnosisId, answers, userId);
    }).isInstanceOf(NotFoundDiagnosisScoreException.class);

    verify(userDiagnosisService, times(0))
        .save(any(UserDiagnosis.class));
  }


  @ParameterizedTest
  @ValueSource(ints = {-1, -3})
  @DisplayName("점수가 음수인 경우 진단 결과 DB 저장 실패 ")
  void createDiagnosisResultFail2(int minusScore) {
    // Arrange
    List<Answer> answers = makeAnswerList();
    List<QuestionBaseLine> baseLines = makeBaseLineList();
    DiagnosisScore diagnosisScore = makeDiagnosisScore();
    UserDiagnosis userDiagnosis
        = makeUserDiagnosis(diagnosisScore);

    int userId = userDiagnosis.getUserId();
    int diagnosisId = userDiagnosis.getDiagnosisId();

    doReturn(baseLines)
        .when(questionBaseLineService)
        .readByDiagnosisIdInCache(diagnosisId);

    doReturn(minusScore)
        .when(scoreCalculator)
        .calc(baseLines, answers);

    // Act , Assert
    assertThatThrownBy(() -> {
      UserDiagnosis result = diagnosisService
          .createDiagnosisResult(diagnosisId, answers, userId);
    }).isInstanceOf(InvalidScoreException.class);

    verify(userDiagnosisService, times(0))
        .save(any(UserDiagnosis.class));
  }
}
