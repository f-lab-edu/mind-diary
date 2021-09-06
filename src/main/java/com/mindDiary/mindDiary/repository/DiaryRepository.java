package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Diary;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryRepository {

  void save(Diary diary);

  Diary findById(int diaryId);

  List<Diary> findByUserId(int userId);
}
