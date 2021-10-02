package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.PostTag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostTagRepository {

  int save(List<PostTag> postTags);

  List<PostTag> findAllByPostIds(List<Integer> postIds);
}
