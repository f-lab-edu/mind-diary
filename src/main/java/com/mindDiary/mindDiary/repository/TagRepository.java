package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagRepository {

  int save(Tag tag);

  Tag findByName(String name);
}
