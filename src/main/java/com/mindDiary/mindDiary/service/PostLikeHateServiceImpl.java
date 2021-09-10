package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.repository.PostLikeHateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeHateServiceImpl implements PostLikeHateService {

  private final PostLikeHateRepository postLikeHateRepository;

  @Override
  public void createLike(int postId, int userId) {
    postLikeHateRepository.saveLike(postId, userId);
  }

  @Override
  public void createHate(int postId, int userId) {
    postLikeHateRepository.saveHate(postId, userId);
  }
}
