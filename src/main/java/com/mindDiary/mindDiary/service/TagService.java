package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Tag;

public interface TagService {

  void save(Tag tag);

  Tag findByName(String name);
}
