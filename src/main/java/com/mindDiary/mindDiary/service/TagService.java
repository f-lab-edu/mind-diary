package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Tag;
import java.util.List;

public interface TagService {

  void save(List<Tag> tags);

  List<Tag> findByNames(List<String> tagNames);
}
