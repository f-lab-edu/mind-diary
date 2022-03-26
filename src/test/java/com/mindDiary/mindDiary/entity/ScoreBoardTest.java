package com.mindDiary.mindDiary.entity;

import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeAnswerList;
import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeBaseLineList;
import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeQuestionList;
import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.Calculator2.Calculator;
import com.mindDiary.mindDiary.strategy.Calculator2.ScoreCalculator2;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScoreBoardTest {

  @Test
  @DisplayName("점수 계산 시 18점이 나와야한다.")
  void calculate() {
    List<Question> questions = makeQuestionList();
    List<QuestionBaseLine> baseLines  = makeBaseLineList();
    List<Answer> answers = makeAnswerList();
    ScoreCalculator2 calculator2 = new Calculator();
    ReverseGenerator generator = new ReverseGenerator();
    ScoreBoard board = new ScoreBoard(questions, baseLines, generator);

    assertThat(board.calc(answers, calculator2)).isSameAs(18);
  }


}
