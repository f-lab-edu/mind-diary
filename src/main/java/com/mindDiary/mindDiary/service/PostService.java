package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Post;
import java.util.List;

public interface PostService {

  void createPost(Post post);

  List<Post> readHotPosts(int pageNumber);

  Post readPost(int postId);
}
