package com.mindDiary.mindDiary.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReverseGenerator {

  public List<Integer> reverse(List<Integer> list) {
    return IntStream.range(0, list.size())
        .mapToObj(i -> list.get(list.size() - 1 - i))
        .collect(Collectors.toList());
  }
}
