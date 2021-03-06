package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.PostMedia;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMediaRepository {

  int save(List<PostMedia> postMedias);

  List<PostMedia> findAllByPostIds(List<Integer> postIds);
}
