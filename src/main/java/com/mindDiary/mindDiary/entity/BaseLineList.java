package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.Calculator2.ScoreCalculator2;
import com.mindDiary.mindDiary.strategy.ScoreListGenerator.ScoreListGeneretor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseLineList {
  private final List<Integer> baseLines = new ArrayList<>();

  public BaseLineList(List<QuestionBaseLine> questionBaseLines, ScoreListGeneretor generator) {
    List<Integer> scoreList = questionBaseLines
        .stream()
        .map(qb -> qb.getScore())
        .collect(Collectors.toList());

    baseLines.addAll(generator.create(scoreList));

  }

  public int calc(int choiceNumber, ScoreCalculator2 calculator) {
    return calculator.calc(baseLines, choiceNumber);
  }
}
