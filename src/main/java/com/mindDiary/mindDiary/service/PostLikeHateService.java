package com.mindDiary.mindDiary.service;

public interface PostLikeHateService {

  void createLike(int postId, int userId);

  void createHate(int postId, int userId);
}
