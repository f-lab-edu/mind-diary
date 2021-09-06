package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.dto.request.CreateDiaryRequestDTO;
import com.mindDiary.mindDiary.dto.response.DiaryResponseDTO;
import com.mindDiary.mindDiary.entity.Diary;
import com.mindDiary.mindDiary.service.DiaryService;
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
@RequestMapping("/diary")
public class DiaryController {

  private final DiaryService diaryService;

  @GetMapping
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<List<DiaryResponseDTO>> readDiaries(Integer userId) {

    List<Diary> diaries = diaryService.readDiaries(userId);

    List<DiaryResponseDTO> diaryResponseDTOS
        = diaries.stream()
        .map(diary -> diary.turnIntoDiaryResponseDTO())
        .collect(Collectors.toList());
    return new ResponseEntity(diaryResponseDTOS, HttpStatus.OK);
  }

  @GetMapping("/{diaryId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity readOneDiary(@PathVariable("diaryId") @Valid int diaryId) {
    Diary diary = diaryService.readOneDiary(diaryId);
    DiaryResponseDTO diaryResponseDTO = diary.turnIntoDiaryResponseDTO();
    return new ResponseEntity(diaryResponseDTO, HttpStatus.OK);
  }

  @PostMapping
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createDiary(@RequestBody @Valid CreateDiaryRequestDTO createDiaryRequestDTO, Integer userId) {
    Diary diary = createDiaryRequestDTO.turnIntoDiaryEntity();
    diaryService.createDiary(diary, userId);
    return new ResponseEntity(HttpStatus.OK);
  }
}
