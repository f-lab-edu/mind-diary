package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepository {

  Question findById(int id);
}
