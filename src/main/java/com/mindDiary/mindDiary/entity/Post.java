package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.dto.request.MediaRequestDTO;
import com.mindDiary.mindDiary.dto.request.TagRequestDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Post {

  private int id;
  private int userId;
  private String writer;
  private LocalDateTime createdAt;
  private String title;
  private String content;
  private int visitCount;
  private int likeCount;
  private int hateCount;
  private int replyCount;
  private List<PostMedia> postMedias;
  private List<Tag> tags;

  public void addPostMedias(List<PostMedia> medias) {
    this.postMedias = new ArrayList<>(medias);
  }

  public void addTags(List<Tag> tags) {
    this.tags = new ArrayList<>(tags);
  }
}
