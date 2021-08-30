package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.DiaryDTO;
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
  public List<DiaryDTO> readDiaries(int userId) {
    return diaryRepository.findByUserId(userId);
  }

  @Override
  public DiaryDTO readOneDiary(int diaryId) {
    return diaryRepository.findById(diaryId);
  }

  @Override
  public void updateDiary(DiaryDTO diaryDTO, int userId) {
    diaryDTO.setUserId(userId);
    diaryDTO.setCreatedAt(LocalDateTime.now());
    diaryRepository.save(diaryDTO);
  }
}
