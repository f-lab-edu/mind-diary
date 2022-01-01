package com.mindDiary.mindDiary.strategy.Calculator;

import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.Reverse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalcByBaseLine implements ScoreCalculator {

  public int calc(List<QuestionBaseLine> baseLines, List<Answer> answers) {

    int size = baseLines.size();

    return answers.stream()
        .mapToInt(a -> baseLines.get(findScoreIndex(a.getChoiceNumber(), a.getReverse(), size)).getScore())
        .sum();
  }

  private int findScoreIndex(int choiceNumber, Reverse reverse, int size) {
    if (reverse.equals(Reverse.TRUE)) {
      return size - choiceNumber - 1;
    }
    return choiceNumber;
  }
}
