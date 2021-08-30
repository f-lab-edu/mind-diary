package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.dto.DiaryDTO;
import com.mindDiary.mindDiary.service.DiaryService;
import com.mindDiary.mindDiary.strategy.jwt.TokenStrategy;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

  private final TokenStrategy tokenStrategy;
  private final DiaryService diaryService;

  @GetMapping
  public ResponseEntity<List<DiaryDTO>> readDiaries(
      @RequestHeader(name = "Authorization") @Valid String token) {

    tokenStrategy.validateToken(token);
    int userId = tokenStrategy.getUserId(token);

    List<DiaryDTO> diaries = diaryService.readDiaries(userId);

    return new ResponseEntity(diaries, HttpStatus.OK);
  }

  @GetMapping("/{diaryId}")
  public ResponseEntity readOneDiary(@RequestHeader(name = "Authorization") @Valid String token,
      @PathVariable("diaryId") @Valid int diaryId) {

    tokenStrategy.validateToken(token);

    DiaryDTO diary = diaryService.readOneDiary(diaryId);
    return new ResponseEntity(diary, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity updateDiary(@RequestHeader(name = "Authorization") @Valid String token,
      @RequestBody @Valid DiaryDTO diaryDTO) {

    tokenStrategy.validateToken(token);

    int userId = tokenStrategy.getUserId(token);

    diaryService.updateDiary(diaryDTO, userId);
    return new ResponseEntity(HttpStatus.OK);
  }
}
