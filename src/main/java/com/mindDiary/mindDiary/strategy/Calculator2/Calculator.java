package com.mindDiary.mindDiary.strategy.Calculator2;


import java.util.List;

public class Calculator implements ScoreCalculator2 {

  @Override
  public int calc(List<Integer> baseLines, int choiceNumber) {
    return baseLines.get(choiceNumber);
  }
}
