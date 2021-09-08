package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.dto.request.CreateDiagnosisResultRequestDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResultResponseDTO;
import com.mindDiary.mindDiary.dto.response.ReadDiagnosisResponseDTO;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.service.DiagnosisService;
import java.util.List;
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
@RequestMapping("/diagnosis")
public class DiagnosisController {

  private final DiagnosisService diagnosisService;

  @GetMapping
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<List<ReadDiagnosisResponseDTO>> readDiagnosis() {

    List<ReadDiagnosisResponseDTO> readDiagnosisResponses
        = diagnosisService.readDiagnosis();

    return new ResponseEntity<>(readDiagnosisResponses, HttpStatus.OK);
  }

  @GetMapping("/question/{diagnosisId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<ReadDiagnosisResponseDTO> readDiagnosisQuestions(
      @PathVariable("diagnosisId") @Valid int diagnosisId) {

    ReadDiagnosisResponseDTO readDiagnosisResponse
        = diagnosisService.readDiagnosisQuestions(diagnosisId);

    return new ResponseEntity<>(readDiagnosisResponse, HttpStatus.OK);
  }

  @PostMapping("/result/{diagnosisId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createDiagnosisResult(@PathVariable("diagnosisId") @Valid int diagnosisId,
      @RequestBody @Valid CreateDiagnosisResultRequestDTO createDiagnosisResultRequest,
      Integer userId) {

    ReadDiagnosisResultResponseDTO readDiagnosisResultResponse
        = diagnosisService
        .createDiagnosisResult(diagnosisId, createDiagnosisResultRequest, userId);

    return new ResponseEntity(readDiagnosisResultResponse, HttpStatus.OK);
  }

  @GetMapping("/mylist")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<ReadDiagnosisResultResponseDTO> readMyDiagnosisResult(Integer userId) {

    List<ReadDiagnosisResultResponseDTO> myDiagnosisResultReponse
        = diagnosisService.readMyDiagnosisResults(userId);

    return new ResponseEntity(myDiagnosisResultReponse, HttpStatus.OK);
  }
}
