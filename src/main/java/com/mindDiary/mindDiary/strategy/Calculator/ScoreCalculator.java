package com.mindDiary.mindDiary.strategy.Calculator;

import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ScoreCalculator {

  int calc(List<QuestionBaseLine> baseLines, List<Answer> answers);
}
