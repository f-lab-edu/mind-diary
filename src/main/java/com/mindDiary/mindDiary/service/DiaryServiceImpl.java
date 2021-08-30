package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.request.DiaryUpdateRequestDTO;
import com.mindDiary.mindDiary.dto.response.DiaryResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

  @Override
  public List<DiaryResponseDTO> readDiaries(int userId) {
    return null;
  }

  @Override
  public DiaryResponseDTO readOneDiary(int userId) {
    return null;
  }

  @Override
  public void updateDiary(DiaryUpdateRequestDTO diaryUpdateRequestDTO, int userId) {

  }
}
