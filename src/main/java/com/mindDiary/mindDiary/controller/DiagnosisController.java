package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.dto.request.CreateDiagnosisRequestDTO;
import com.mindDiary.mindDiary.dto.request.CreateDiagnosisResultRequestDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResultResponseDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResponseDTO;
import com.mindDiary.mindDiary.entity.Diagnosis;
import com.mindDiary.mindDiary.entity.Answer;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.UserDiagnosis;
import com.mindDiary.mindDiary.service.DiagnosisService;
import com.mindDiary.mindDiary.service.UserDiagnosisService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnoses")
public class DiagnosisController {

  private final DiagnosisService diagnosisService;
  private final UserDiagnosisService userDiagnosisService;

  @PostMapping("/save")
  @LoginCheck(checkLevel = Role.ADMIN)
  public ResponseEntity saveInCache() {
    diagnosisService.saveAll();
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/{pageNumber}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<List<ReadDiagnosisResponseDTO>> readDiagnoses(@PathVariable @Valid int pageNumber) {
    List<Diagnosis> diagnoses = diagnosisService.readAll(pageNumber);
    return new ResponseEntity<>(createReadDiagnosisResponses(diagnoses), HttpStatus.OK);
  }

  @GetMapping("/{diagnosisId}/question")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<ReadDiagnosisResponseDTO> readDiagnosisQuestions(
      @PathVariable("diagnosisId") @Valid int diagnosisId) {

    Diagnosis diagnosis
        = diagnosisService.readOne(diagnosisId);

    return new ResponseEntity<>(ReadDiagnosisResponseDTO.create(diagnosis),
        HttpStatus.OK);
  }

  @PostMapping("/{diagnosisId}/result")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createDiagnosisResult(@PathVariable("diagnosisId") @Valid int diagnosisId,
      @RequestBody @Valid CreateDiagnosisResultRequestDTO createDiagnosisResultRequest,
      Integer userId) {

    List<Answer> answers = createAnswers(createDiagnosisResultRequest);

    UserDiagnosis userDiagnosis
        = diagnosisService.createDiagnosisResult(diagnosisId, answers, userId);

    return new ResponseEntity(ReadDiagnosisResultResponseDTO.create(userDiagnosis), HttpStatus.OK);
  }

  @GetMapping("/mylist")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<ReadDiagnosisResultResponseDTO> readMyDiagnosisResult(Integer userId) {

    List<UserDiagnosis> userDiagnosis = userDiagnosisService.readMyDiagnosisResults(userId);

    List<ReadDiagnosisResultResponseDTO> myDiagnosisResultReponses
        = createReadDiagnosisResultResponse(userDiagnosis);

    return new ResponseEntity(myDiagnosisResultReponses, HttpStatus.OK);
  }


  @PostMapping
  @LoginCheck(checkLevel = Role.ADMIN)
  public ResponseEntity createDiagnosis(@RequestBody @Valid CreateDiagnosisRequestDTO createDiagnosisRequest) {

    diagnosisService.create(createDiagnosisRequest);

    return new ResponseEntity(HttpStatus.OK);
  }

  private List<ReadDiagnosisResponseDTO> createReadDiagnosisResponses(List<Diagnosis> diagnoses) {
    return diagnoses.stream()
        .map(d -> ReadDiagnosisResponseDTO.create(d))
        .collect(Collectors.toList());
  }

  private List<ReadDiagnosisResultResponseDTO> createReadDiagnosisResultResponse(
      List<UserDiagnosis> userDiagnosis) {
    return userDiagnosis.stream()
        .map(ud -> ReadDiagnosisResultResponseDTO.create(ud))
        .collect(Collectors.toList());
  }

  private List<Answer> createAnswers(
      CreateDiagnosisResultRequestDTO createDiagnosisResultRequestDTO) {
    return createDiagnosisResultRequestDTO.getQuestionAnswerRequests()
        .stream().map(q -> new Answer(q.getQuestionId(), q.getChoiceNumber(), q.getReverse()))
        .collect(Collectors.toList());
  }

}
