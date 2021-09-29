package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.repository.PostLikeHateRepository;
import com.mindDiary.mindDiary.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeHateServiceImpl implements PostLikeHateService {

  private final PostLikeHateRepository postLikeHateRepository;
  private final PostRepository postRepository;

  @Override
  @Transactional
  public void createLike(int postId, int userId) {
    postLikeHateRepository.saveLike(postId, userId);
    postRepository.increaseLikeCount(postId);
  }

  @Override
  @Transactional
  public void createHate(int postId, int userId) {
    postLikeHateRepository.saveHate(postId, userId);
    postRepository.increaseHateCount(postId);
  }
}
