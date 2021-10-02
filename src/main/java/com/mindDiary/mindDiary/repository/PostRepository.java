package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.PageCriteria;
import com.mindDiary.mindDiary.entity.Post;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostRepository {

  int save(Post post);

  List<Post> findHotPosts(PageCriteria pageCriteria);

  Post findById(int postId);

  int increaseVisitCount(int postId);

  int increaseLikeCount(int postId);

  int increaseHateCount(int postId);
}
