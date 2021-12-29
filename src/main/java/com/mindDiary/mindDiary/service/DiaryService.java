package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Diary;
import java.util.List;

public interface DiaryService {

  List<Diary> readDiaries(int userId);

  Diary readOneDiary(int userId);

  void createDiary(Diary diary);
}
