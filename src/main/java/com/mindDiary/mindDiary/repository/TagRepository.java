package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagRepository {

  Tag findByName(String name);

  int save(List<Tag> tags);
}
