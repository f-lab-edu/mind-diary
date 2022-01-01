package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.Calculator2.ScoreCalculator2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreBoard {

  private Map<Integer, List<Integer>> baseLineMap;

  public ScoreBoard(Map<Integer, List<Integer>> baseLineMap) {
    this.baseLineMap = baseLineMap;
  }

  public ScoreBoard(List<Question> questions, List<QuestionBaseLine> baseLines) {

    ReverseGenerator generator = new ReverseGenerator();
    List<Integer> baseLineIntList = baseLines.stream()
        .map(b -> b.getScore())
        .collect(Collectors.toList());

    baseLineMap = new HashMap<>();
    for (Question q : questions) {
      baseLineMap.put(q.getId(), new ArrayList<>());
      baseLineMap.get(q.getId()).addAll(q.isReverse() ? generator.reverse(baseLineIntList) : baseLineIntList);
    }
  }

  public int calc(List<Answer> answers, ScoreCalculator2 calculator) {
    return answers.stream()
        .mapToInt(a -> calculator.calc(
            baseLineMap.get(a.getQuestionId()),
            a.getChoiceNumber()
        ))
        .sum();
  }

}
