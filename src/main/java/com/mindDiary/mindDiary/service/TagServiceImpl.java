package com.mindDiary.mindDiary.service;


import com.mindDiary.mindDiary.entity.Tag;
import com.mindDiary.mindDiary.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  public void save(List<Tag> tags) {
    tagRepository.save(tags);
  }

  @Override
  public List<Tag> findByNames(List<String> tagNames) {
    return tagRepository.findByNames(tagNames);
  }

}
