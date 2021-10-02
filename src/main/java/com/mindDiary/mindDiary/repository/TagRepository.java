package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagRepository {

  int save(List<Tag> tags);

  List<Tag> findByNames(List<String> tagNames);
}
