package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.strategy.Calculator2.ScoreCalculator2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoard {

  private final Map<Integer, BaseLineList> baseLineMap = new HashMap<>();

  public ScoreBoard(List<Question> questions, List<QuestionBaseLine> baseLines) {
    questions.forEach(q -> put(q.getId(), new BaseLineList(baseLines, q.createScoreListGenerator())));
  }

  public void put(int questionId, BaseLineList baseLines) {
    baseLineMap.put(questionId, baseLines);
  }

  public int calc(List<Answer> answers, ScoreCalculator2 calculator) {
    return answers.stream()
        .mapToInt(a -> calc(a, calculator))
        .sum();
  }

  public int calc(Answer answer, ScoreCalculator2 calculator) {
    return baseLineMap.get(answer.getQuestionId()).calc(answer.getChoiceNumber(), calculator);
  }

}
