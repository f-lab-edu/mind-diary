package com.mindDiary.mindDiary.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.ScoreListGenerator.NormalScoreListGenerator;
import com.mindDiary.mindDiary.strategy.ScoreListGenerator.ReverseScoreListGenerator;
import com.mindDiary.mindDiary.strategy.ScoreListGenerator.ScoreListGeneretor;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScoreListGeneratorTest {

  @Test
  @DisplayName("배열을 뒤집어서 점수표를 만든다.")
  void create_score_list_by_reversing_the_array() {
    List<Integer> list = Arrays.asList(1,2,3,4);
    ScoreListGeneretor generator = new ReverseScoreListGenerator();
    assertThat(generator.create(list))
        .isEqualTo(Arrays.asList(4,3,2,1));
  }
  @Test
  @DisplayName("배열을 뒤집지 않고 점수표를 만든다.")
  void create_score_list() {
    ScoreListGeneretor generator = new NormalScoreListGenerator();
    List<Integer> list = Arrays.asList(1,2,3,4);
    assertThat(generator.create(list))
        .isEqualTo(Arrays.asList(1,2,3,4));
  }

}
