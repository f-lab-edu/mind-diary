package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Tag;
import java.util.List;

public interface TagService {

  Tag findByName(String name);

  void save(List<Tag> tags);
}
