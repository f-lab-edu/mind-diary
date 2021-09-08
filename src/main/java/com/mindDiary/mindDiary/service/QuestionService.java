package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.Answer;
import java.util.List;

public interface QuestionService {

  List<Question> readByAnswers(List<Answer> answers);
}
