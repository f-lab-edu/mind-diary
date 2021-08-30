package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.dto.DiaryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryRepository {

  void save(DiaryDTO diaryDTO);

  DiaryDTO findById(int diaryId);

  List<DiaryDTO> findByUserId(int userId);
}
