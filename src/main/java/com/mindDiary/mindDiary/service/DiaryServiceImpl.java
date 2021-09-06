package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diary;
import com.mindDiary.mindDiary.repository.DiaryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

  private final DiaryRepository diaryRepository;

  @Override
  public List<Diary> readDiaries(int userId) {
    return diaryRepository.findByUserId(userId);
  }

  @Override
  public Diary readOneDiary(int diaryId) {
    return diaryRepository.findById(diaryId);
  }

  @Override
  public void createDiary(Diary diary, int userId) {
    diary.setUserId(userId);
    diary.setCreatedAt(LocalDateTime.now());
    diaryRepository.save(diary);
  }
}
