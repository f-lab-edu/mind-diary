package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.repository.PostMediaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl implements PostMediaService {

  private final PostMediaRepository postMediaRepository;

  @Override
  public void save(List<PostMedia> postMedias) {
    postMediaRepository.save(postMedias);
  }
}
