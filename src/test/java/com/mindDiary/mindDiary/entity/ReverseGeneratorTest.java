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
    assertThat(generator.reverse(list))
        .isEqualTo(Arrays.asList(4,3,2,1));
  }
}
