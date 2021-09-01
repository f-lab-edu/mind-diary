package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.annotation.LoginCheck.CheckLevel;
import com.mindDiary.mindDiary.dto.DiaryDTO;
import com.mindDiary.mindDiary.service.DiaryService;
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
@RequestMapping("/diary")
public class DiaryController {

  private final DiaryService diaryService;

  @GetMapping
  @LoginCheck(checkLevel = CheckLevel.USER)
  public ResponseEntity<List<DiaryDTO>> readDiaries(Integer userId) {

    List<DiaryDTO> diaries = diaryService.readDiaries(userId);

    return new ResponseEntity(diaries, HttpStatus.OK);
  }

  @GetMapping("/{diaryId}")
  @LoginCheck(checkLevel = CheckLevel.USER)
  public ResponseEntity readOneDiary(@PathVariable("diaryId") @Valid int diaryId) {
    DiaryDTO diary = diaryService.readOneDiary(diaryId);
    return new ResponseEntity(diary, HttpStatus.OK);
  }

  @PostMapping
  @LoginCheck(checkLevel = CheckLevel.USER)
  public ResponseEntity updateDiary(@RequestBody @Valid DiaryDTO diaryDTO, Integer userId) {
    diaryService.updateDiary(diaryDTO, userId);
    return new ResponseEntity(HttpStatus.OK);
  }
}
