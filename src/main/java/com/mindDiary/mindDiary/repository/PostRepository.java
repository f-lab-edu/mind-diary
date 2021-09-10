package com.mindDiary.mindDiary.repository;

import com.mindDiary.mindDiary.entity.Post;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostRepository {

  int save(Post post);

  List<Post> findHotPosts();

  Post findById(int postId);

  int increaseVisitCount(int postId);
}
