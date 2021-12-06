package com.mindDiary.mindDiary.strategy.scoreCalc;

import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScoreCalculator {

  private final ScoreCalculateStrategy strategy;

  public int calc(List<QuestionBaseLine> baseLines, List<Answer> answers) {
    strategy.addScoreBaseLine(createIntegerBaseLine(baseLines));
    return answers.stream()
        .mapToInt(a -> strategy.calc(a.getChoiceNumber(), a.getReverse()))
        .sum();
  }

  public List<Integer> createIntegerBaseLine(List<QuestionBaseLine> questionBaseLines) {
    return questionBaseLines.stream()
        .map(qb -> qb.getScore())
        .collect(Collectors.toList());
  }
}
