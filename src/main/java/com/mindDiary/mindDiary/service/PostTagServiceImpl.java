package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.PostTag;
import com.mindDiary.mindDiary.mapper.PostTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostTagServiceImpl implements PostTagService {

  private final PostTagRepository postTagRepository;

  @Override
  public void save(List<PostTag> postTags) {
    postTagRepository.save(postTags);
  }

  @Override
  public List<PostTag> findAllByPostIds(List<Integer> postIds) {
    return postTagRepository.findAllByPostIds(postIds);
  }
}
