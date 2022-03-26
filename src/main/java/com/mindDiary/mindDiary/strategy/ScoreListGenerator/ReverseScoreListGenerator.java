package com.mindDiary.mindDiary.strategy.ScoreListGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReverseScoreListGenerator implements
    ScoreListGeneretor {

  @Override
  public List<Integer> create(List<Integer> list) {
    return IntStream.range(0, list.size())
        .mapToObj(i -> list.get(list.size() - 1 - i))
        .collect(Collectors.toList());
  }
}
