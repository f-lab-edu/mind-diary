package com.mindDiary.mindDiary.strategy.scoreCalc;

import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.Reverse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ScoreCalculateStrategy {

  int calc(List<QuestionBaseLine> baseLines, List<Answer> answers);
}
