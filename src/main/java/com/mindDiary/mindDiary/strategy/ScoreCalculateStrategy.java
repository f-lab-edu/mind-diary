package com.mindDiary.mindDiary.strategy;

import com.mindDiary.mindDiary.entity.Question.Reverse;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.ArrayList;
import java.util.List;

public class ScoreCalculateStrategy {

  private List<Integer> baseLineScores;
  private int size;

  public ScoreCalculateStrategy(List<Integer> scores) {
    this.baseLineScores = scores;
    this.size = scores.size();
  }

  public static ScoreCalculateStrategy createScores(List<QuestionBaseLine> questionBaseLines) {
    List<Integer> scores = new ArrayList<>();
    for (QuestionBaseLine qb : questionBaseLines) {
      scores.add(qb.getScore());
    }
    return new ScoreCalculateStrategy(scores);
  }

  public int calc(int choiceNumber, Reverse reverse) {
    if (reverse == Reverse.TRUE) {
      return baseLineScores.get(size - choiceNumber - 1);
    }
    return baseLineScores.get(choiceNumber);
  }
}
