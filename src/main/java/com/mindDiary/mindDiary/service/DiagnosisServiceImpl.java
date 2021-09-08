package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.request.CreateDiagnosisResultRequestDTO;
import com.mindDiary.mindDiary.dto.request.QuestionAnswerRequestDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResultResponseDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResponseDTO;
import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.DiagnosisWithQuestion;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.Question.Reverse;
import com.mindDiary.mindDiary.entity.QuestionAnswer;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.repository.DiagnosisRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

  private final DiagnosisRepository diagnosisRepository;

  @Override
  public List<ReadDiagnosisResponseDTO> readDiagnosis() {
    List<Diagnosis> diagnosisList = diagnosisRepository.findDiagnosis();

    List<ReadDiagnosisResponseDTO> readDiagnosisResponses = new ArrayList<>();

    diagnosisList.forEach(diagnosis -> readDiagnosisResponses.add(
        ReadDiagnosisResponseDTO.create(diagnosis)));

    return readDiagnosisResponses;
  }

  @Override
  public ReadDiagnosisResponseDTO readDiagnosisQuestions(int diagnosisId) {

    DiagnosisWithQuestion diagnosisWithQuestion
        = diagnosisRepository.findDiagnosisWithQuestionById(diagnosisId);

    return ReadDiagnosisResponseDTO.create(diagnosisWithQuestion);
  }

  @Override
  public ReadDiagnosisResultResponseDTO createDiagnosisResult(int diagnosisId,
      CreateDiagnosisResultRequestDTO createDiagnosisResultRequest, int userId) {

    List<QuestionAnswer> questionAnswer = new ArrayList<>();
    for (QuestionAnswerRequestDTO qa : createDiagnosisResultRequest.getQuestionAnswerRequests()) {
      questionAnswer.add(QuestionAnswer.create(qa.getQuestionId(), qa.getChoiceNumber()));
    }

    List<QuestionBaseLine> questionBaseLines
        = diagnosisRepository.findQuestionBaseLinesById(diagnosisId);

    List<Question> questions = diagnosisRepository.findQuestionsByAnswers(questionAnswer);

    int score = 0;
    int questionSize = questions.size();
    int baseLineSize = questionBaseLines.size();

    for (int i = 0; i < questionSize; i++) {
      Question q = questions.get(i);
      int choiceNumber = questionAnswer.get(i).getChoiceNumber();
      if (q.getReverse() == Reverse.TRUE) {
        score += questionBaseLines.get(baseLineSize - choiceNumber - 1).getScore();
      } else {
        score += questionBaseLines.get(choiceNumber).getScore();
      }
    }

    DiagnosisScore diagnosisScores = diagnosisRepository.findDiagnosisScore(diagnosisId, score);
    String content = diagnosisScores.getContent();

    UserDiagnosis userDiagnosis = UserDiagnosis
        .create(userId, diagnosisId, LocalDateTime.now(), score, content);
    diagnosisRepository.save(userDiagnosis);

    ReadDiagnosisResultResponseDTO diagnosisResultScoreResponse
        = ReadDiagnosisResultResponseDTO.create(userDiagnosis);

    return diagnosisResultScoreResponse;
  }

  @Override
  public List<ReadDiagnosisResultResponseDTO> readMyDiagnosisResults(int userId) {
    List<UserDiagnosis> userDiagnosis = diagnosisRepository.findUserDiagnosisById(userId);

    List<ReadDiagnosisResultResponseDTO> myDiagnosisResultReponses = new ArrayList<>();
    userDiagnosis.forEach(ud -> myDiagnosisResultReponses.add(
        ReadDiagnosisResultResponseDTO.create(ud)));

    return myDiagnosisResultReponses;
  }
}
