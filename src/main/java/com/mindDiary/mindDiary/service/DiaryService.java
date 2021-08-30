package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.DiaryDTO;
import java.util.List;

public interface DiaryService {

  List<DiaryDTO> readDiaries(int userId);

  DiaryDTO readOneDiary(int userId);

  void updateDiary(DiaryDTO diaryDTO, int userId);
}
