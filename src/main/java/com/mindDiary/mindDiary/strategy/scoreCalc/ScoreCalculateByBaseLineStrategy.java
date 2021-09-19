package com.mindDiary.mindDiary.strategy.scoreCalc;

import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.Reverse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScoreCalculateByBaseLineStrategy implements ScoreCalculateStrategy {
  private List<Integer> scores = new ArrayList<>();
  private int size;

  @Override
  public void addScoreBaseLine(List<Integer> baseLine) {
    scores.addAll(baseLine);
    size = baseLine.size();
  }

  @Override
  public int calc(int choiceNumber, Reverse reverse) {
    if (reverse == Reverse.TRUE) {
      return scores.get(size - choiceNumber - 1);
    }
    return scores.get(choiceNumber);
  }

}
