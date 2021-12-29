package com.mindDiary.mindDiary.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostLikeHateRepository {

  int saveLike(int postId, int userId);

  int saveHate(int postId, int userId);
}
