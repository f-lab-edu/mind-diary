package com.mindDiary.mindDiary.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReverseGeneratorTest {

  @Test
  @DisplayName("배열을 뒤집는다.")
  void reverse_list() {
    List<Integer> list = Arrays.asList(1,2,3,4);
    ReverseGenerator generator = new ReverseGenerator();
    assertThat(generator.reverseOrNot(list, Reverse.TRUE))
        .isEqualTo(Arrays.asList(4,3,2,1));
  }

  @Test
  @DisplayName("배열을 뒤집히지 않는다.")
  void origin_list() {
    List<Integer> list = Arrays.asList(1,2,3,4);
    ReverseGenerator generator = new ReverseGenerator();
    assertThat(generator.reverseOrNot(list, Reverse.FALSE))
        .isEqualTo(Arrays.asList(1,2,3,4));
  }
}
