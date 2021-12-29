package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.PostMedia;
import java.util.Arrays;
import java.util.List;

public interface PostMediaService {

  void save(List<PostMedia> postMedias);

  List<PostMedia> findAllByPostIds(List<Integer> postIds);
}
