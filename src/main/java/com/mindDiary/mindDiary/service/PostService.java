package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.Tag;
import java.util.List;

public interface PostService {

  void createPost(Post post, List<Tag> tags);

  List<Post> readHotPosts(int pageNumber);

  Post readPost(int postId);
}
