package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.dto.request.DiaryUpdateRequestDTO;
import com.mindDiary.mindDiary.dto.response.DiaryResponseDTO;
import java.util.List;

public interface DiaryService {

  List<DiaryResponseDTO> readDiaries(int userId);

  DiaryResponseDTO readOneDiary(int userId);

  void updateDiary(DiaryUpdateRequestDTO diaryUpdateRequestDTO, int userId);
}
