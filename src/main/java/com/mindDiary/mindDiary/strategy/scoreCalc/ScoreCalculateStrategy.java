package com.mindDiary.mindDiary.strategy.scoreCalc;

import com.mindDiary.mindDiary.entity.Reverse;
import java.util.List;

public interface ScoreCalculateStrategy {

  int calc(int choiceNumber, Reverse reverse);

  void addScoreBaseLine(List<Integer> baseLine);
}
