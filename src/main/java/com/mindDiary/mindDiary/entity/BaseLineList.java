package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.Calculator2.ScoreCalculator2;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseLineList {
  private final List<Integer> baseLines = new ArrayList<>();

  public BaseLineList(List<QuestionBaseLine> questionBaseLines, Reverse reverse, ReverseGenerator generator) {
    List<Integer> scores = questionBaseLines
        .stream()
        .map(qb -> qb.getScore())
        .collect(Collectors.toList());

    baseLines.addAll(generator.reverseOrNot(scores, reverse));
  }

  public int calc(int choiceNumber, ScoreCalculator2 calculator) {
    return calculator.calc(baseLines, choiceNumber);
  }
}
