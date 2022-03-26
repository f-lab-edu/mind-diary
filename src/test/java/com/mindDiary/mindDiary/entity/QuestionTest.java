package com.mindDiary.mindDiary.entity;



import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeCreateQuestionRequestDTO;
import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeQuestion;
import static com.mindDiary.mindDiary.service.diagnosis.DiagnosisDummy.makeQuestionList;
import static org.assertj.core.api.Assertions.assertThat;

import com.mindDiary.mindDiary.strategy.ScoreListGenerator.NormalScoreListGenerator;
import com.mindDiary.mindDiary.strategy.ScoreListGenerator.ReverseScoreListGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {

  @Test
  @DisplayName("질문 계산을 거꾸로 하지 않을 경우 NormalScoreListGenerator를 만든다.")
  void create_normal_score_list_generator_cause_question_is_not_reverse() {
    Question question = new Question(1,1,"content",Reverse.FALSE);

    assertThat(question.getReverse()).isEqualTo(Reverse.FALSE);
    assertThat(question.createScoreListGenerator())
        .isInstanceOf(NormalScoreListGenerator.class);
  }


  @Test
  @DisplayName("질문 계산을 거꾸로 해야할 경우 ReverseScoreListGenerator를 만든다.")
  void create_reverse_score_list_generator_cause_question_is_reverse() {
    Question question = new Question(1,1,"content",Reverse.TRUE);

    assertThat(question.getReverse()).isEqualTo(Reverse.TRUE);
    assertThat(question.createScoreListGenerator())
        .isInstanceOf(ReverseScoreListGenerator.class);
  }
}
