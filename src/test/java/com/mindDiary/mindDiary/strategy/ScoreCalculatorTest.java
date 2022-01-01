package com.mindDiary.mindDiary.strategy;


import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeAnswerList;
import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeBaseLineList;
import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeQuestionList;
import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.Calculator2.Calculator;
import com.mindDiary.mindDiary.strategy.Calculator2.ScoreCalculator2;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ScoreCalculatorTest {

  @Test
  @DisplayName("1번 질문에 0번을 선택시 점수는 0점이어야 한다. ")
  void calculate() {
    ScoreCalculator2 sut = new Calculator();
    List<Integer> baseLines = Arrays.asList(0,1,2,3);

    assertThat(sut.calc(baseLines, 0)).isSameAs(0);
  }


  @Test
  @DisplayName("1번 질문에 3번을 선택시 점수는 3점이어야 한다.")
  void calculate2() {
    ScoreCalculator2 sut = new Calculator();
    List<Integer> baseLines = Arrays.asList(0,1,2,3);

    assertThat(sut.calc(baseLines, 3)).isSameAs(3);
  }


}
