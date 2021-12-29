package com.mindDiary.mindDiary.service.diagnosis;

import com.mindDiary.mindDiary.dto.request.CreateDiagnosisRequestDTO;
import com.mindDiary.mindDiary.dto.request.CreateDiagnosisScoreRequestDTO;
import com.mindDiary.mindDiary.dto.request.CreateQuestionBaseLineRequestDTO;
import com.mindDiary.mindDiary.dto.request.CreateQuestionRequestDTO;
import com.mindDiary.mindDiary.dto.request.QuestionAnswerRequestDTO;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.DiagnosisScore;
import com.mindDiary.mindDiary.entity.Question;
import com.mindDiary.mindDiary.entity.QuestionBaseLine;
import com.mindDiary.mindDiary.entity.Reverse;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiagnosisDummy {

  static Diagnosis makeDiagnosis() {
    return makeDiagnosis(makeCreateDiagnosisRequestDTO());
  }

  static Diagnosis makeDiagnosis(CreateDiagnosisRequestDTO request) {
    return Diagnosis.builder()
        .questionBaseLines(makeBaseLineList())
        .questions(makeQuestionList())
        .name(request.getName())
        .id(1)
        .numberOfChoice(request.getNumberOfChoice())
        .build();
  }

  static CreateDiagnosisRequestDTO makeCreateDiagnosisRequestDTO(
      List<CreateQuestionRequestDTO> questions,
      List<CreateQuestionBaseLineRequestDTO> questionBaseLines,
      List<CreateDiagnosisScoreRequestDTO> diagnosisScores
  ) {
    return new CreateDiagnosisRequestDTO(
        "스트레스(PSS)",
        10,
        questions,
        questionBaseLines,
        diagnosisScores
    );
  }


  static CreateDiagnosisRequestDTO
  makeCreateDiagnosisRequestDTO() {
    List<CreateQuestionRequestDTO> questions =
        makeCreateQuestionRequestDTOList();

    List<CreateQuestionBaseLineRequestDTO> questionBaseLines =
        makeCreateQuestionBaseLineRequestDTOList();

    List<CreateDiagnosisScoreRequestDTO> diagnosisScores =
        makeCreateDiagnosisScoreRequestDTOList();

    return makeCreateDiagnosisRequestDTO(
        questions,
        questionBaseLines,
        diagnosisScores);
  }

  static List<DiagnosisScore> makeScoreList() {
    List<CreateDiagnosisScoreRequestDTO> scores =
        makeCreateDiagnosisScoreRequestDTOList();
    return makeScoreList(scores);
  }


  static List<DiagnosisScore> makeScoreList(
      List<CreateDiagnosisScoreRequestDTO> scores) {
    return IntStream.range(0, scores.size())
        .mapToObj(i -> makeScoreEntity(i + 1, scores.get(i)))
        .collect(Collectors.toList());
  }

  static CreateDiagnosisScoreRequestDTO
  makeCreateDiagnosisScoreRequestDTO() {
    return new CreateDiagnosisScoreRequestDTO(
        18,
        25,
        "경도의 스트레스"
    );
  }

  static List<Answer> makeAnswerList() {
    return makeQuestionAnswerRequestDTOList()
        .stream()
        .map(a -> new Answer(
            a.getQuestionId(),
            a.getChoiceNumber(),
            a.getReverse()))
        .collect(Collectors.toList());
  }

  static UserDiagnosis makeUserDiagnosis() {
    return makeUserDiagnosis(makeDiagnosisScore());
  }

  static List<QuestionAnswerRequestDTO>
  makeQuestionAnswerRequestDTOList() {
    List<QuestionAnswerRequestDTO> answers
        = new ArrayList<>();
    answers.add(
        new QuestionAnswerRequestDTO(
            1, 3, Reverse.FALSE));

    answers.add(
        new QuestionAnswerRequestDTO(
            2, 2, Reverse.FALSE));

    answers.add(
        new QuestionAnswerRequestDTO(
            3, 0, Reverse.FALSE));

    answers.add(
        new QuestionAnswerRequestDTO(
            4, 0, Reverse.TRUE));

    answers.add(
        new QuestionAnswerRequestDTO(
            5, 1, Reverse.TRUE));

    answers.add(
        new QuestionAnswerRequestDTO(
            6, 2, Reverse.FALSE));

    answers.add(
        new QuestionAnswerRequestDTO(
            7, 0, Reverse.TRUE));

    answers.add(
        new QuestionAnswerRequestDTO(
            8, 0, Reverse.TRUE));

    answers.add(
        new QuestionAnswerRequestDTO(
            9, 0, Reverse.FALSE));

    answers.add(
        new QuestionAnswerRequestDTO(
            10, 0, Reverse.FALSE));

    return answers;
  }

  public static UserDiagnosis makeUserDiagnosis(DiagnosisScore diagnosisScore) {
    return UserDiagnosis.builder()
        .diagnosisId(diagnosisScore.getDiagnosisId())
        .content(diagnosisScore.getContent())
        .createdAt(LocalDateTime.now())
        .score(18)
        .userId(1)
        .build();
  }

  static DiagnosisScore makeDiagnosisScore() {
    return makeScoreEntity(2, makeCreateDiagnosisScoreRequestDTO());
  }

  static DiagnosisScore makeScoreEntity(
      int id,
      CreateDiagnosisScoreRequestDTO dto) {
    return DiagnosisScore.builder()
        .content(dto.getContent())
        .diagnosisId(1)
        .maxValue(dto.getMaxValue())
        .minValue(dto.getMinValue())
        .id(id)
        .build();
  }

  static List<CreateDiagnosisScoreRequestDTO>
  makeCreateDiagnosisScoreRequestDTOList() {
    List<CreateDiagnosisScoreRequestDTO> scores = new ArrayList<>();

    scores.add(new CreateDiagnosisScoreRequestDTO(
        0,
        17,
        "정상"
    ));
    scores.add(new CreateDiagnosisScoreRequestDTO(
        18,
        25,
        "경도의 스트레스"
    ));
    scores.add(new CreateDiagnosisScoreRequestDTO(
        26,
        30,
        "고도의 스트레스"
    ));

    return scores;
  }


  static CreateQuestionBaseLineRequestDTO
  makeCreateQuestionBaseLineRequestDTO() {
    return new CreateQuestionBaseLineRequestDTO(
        "아니다",
        0
    );
  }


  static List<QuestionBaseLine> makeBaseLineList() {
    List<CreateQuestionBaseLineRequestDTO> baseLines =
        makeCreateQuestionBaseLineRequestDTOList();
    return makeBaseLineList(baseLines);
  }

  static List<QuestionBaseLine> makeBaseLineList(
      List<CreateQuestionBaseLineRequestDTO> baseLines) {
    return IntStream.range(0, baseLines.size())
        .mapToObj(i -> makeBaseLine(i + 1, baseLines.get(i)))
        .collect(Collectors.toList());
  }

  public static QuestionBaseLine makeBaseLine(
      int id,
      CreateQuestionBaseLineRequestDTO dto) {
    return QuestionBaseLine.builder()
        .diagnosisId(1)
        .score(dto.getScore())
        .content(dto.getContent())
        .id(id)
        .build();
  }

  static List<CreateQuestionBaseLineRequestDTO>
  makeCreateQuestionBaseLineRequestDTOList() {
    List<CreateQuestionBaseLineRequestDTO> baseLines
        = new ArrayList<>();
    baseLines.add(new CreateQuestionBaseLineRequestDTO(
        "아니다",
        0
    ));
    baseLines.add(new CreateQuestionBaseLineRequestDTO(
        "가끔 그렇다",
        1
    ));
    baseLines.add(new CreateQuestionBaseLineRequestDTO(
        "자주 그렇다",
        2
    ));
    baseLines.add(new CreateQuestionBaseLineRequestDTO(
        "항상 그렇다",
        3
    ));

    return baseLines;
  }


  static CreateQuestionRequestDTO
  makeCreateQuestionRequestDTO() {
    return new CreateQuestionRequestDTO(
        "예상치 못한 일 때문에 화가 난 적이 있습니까?",
        Reverse.FALSE
    );
  }


  static List<Question> makeQuestionList() {
    List<CreateQuestionRequestDTO> questions =
        makeCreateQuestionRequestDTOList();
    return makeQuestionList(questions);
  }

  static List<Question> makeQuestionList(
      List<CreateQuestionRequestDTO> questions) {
    return IntStream.range(0, questions.size())
        .mapToObj(i -> makeQuestion(i + 1, questions.get(i)))
        .collect(Collectors.toList());
  }


  static Question makeQuestion(
      int id,
      CreateQuestionRequestDTO createQuestionRequestDTO) {
    return Question.builder()
        .reverse(createQuestionRequestDTO.getReverse())
        .content(createQuestionRequestDTO.getContent())
        .diagnosisId(1)
        .id(id)
        .build();
  }


  static List<CreateQuestionRequestDTO> makeCreateQuestionRequestDTOList() {
    List<CreateQuestionRequestDTO> questions
        = new ArrayList<>();
    questions.add(new CreateQuestionRequestDTO(
        "예상치 못한 일 때문에 화가 난 적이 있습니까?",
        Reverse.FALSE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "생활하면서 중요한 일들을 통제할 수 없다고 느낀 적이 있습니까?",
        Reverse.FALSE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "신경이 예민해지고 스트레스를 받은 적이 있습니까?",
        Reverse.FALSE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "개인적인 문제들을 다루는 능력에 대해 자신감을 느낀 적이 있습니까?",
        Reverse.TRUE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "당신이 원하는 방식으로 일이 진행되고 있다고 느낀 적이 있습니까?",
        Reverse.TRUE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "당신이 해야만 하는 모든 일을 감당할 수 없다고 느낀 적이 있습니까?",
        Reverse.FALSE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "일상생활에서 겪는 불안감과 초조함을 통제할 수 있었습니까?",
        Reverse.TRUE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "일들이 어떻게 돌아가는 지 잘 알고 있다고 느낀 적이 있었습니까?",
        Reverse.TRUE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "통제할 수 없는 일 때문에 화가 난 적이 있습니까?",
        Reverse.FALSE
    ));

    questions.add(new CreateQuestionRequestDTO(
        "힘든 일이 너무 많이 쌓여서 도저히 감당할 수 없다고 느낀 적이 있습니까?",
        Reverse.FALSE
    ));

    return questions;
  }
}
