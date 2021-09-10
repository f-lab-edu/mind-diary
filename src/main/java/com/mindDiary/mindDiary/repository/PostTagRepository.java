package com.mindDiary.mindDiary.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostTagRepository {

  int save(int postId, int tagId);
}
