package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;

  @Override
  public List<Question> readByAnswers(
      List<Answer> answers) {

    List<Question> questions = new ArrayList<>();
    for (Answer qa : answers) {
      Question question = questionRepository.findById(qa.getQuestionId());
      question.setChoiceNumber(qa.getChoiceNumber());
      questions.add(question);
    }
    return questions;
  }
}
