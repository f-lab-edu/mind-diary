package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostTagServiceImpl implements PostTagService {

  private final PostTagRepository postTagRepository;

  @Override
  public void save(int postId, int tagId) {
    postTagRepository.save(postId, tagId);
  }
}
