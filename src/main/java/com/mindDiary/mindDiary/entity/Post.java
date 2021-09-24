package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

  public Post(int userId, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount,
      List<PostMedia> postMedias, List<Tag> tags) {

    this.userId = userId;
    this.createdAt = createdAt;
    this.title = title;
    this.content = content;
    this.visitCount = visitCount;
    this.likeCount = likeCount;
    this.hateCount = hateCount;
    this.replyCount = replyCount;
    this.postMedias = new ArrayList<>(postMedias);
    this.tags = new ArrayList<>(tags);
  }

  public static Post create(int userId, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount,
      List<PostMedia> postMedias, List<Tag> tags) {

    return new Post(
        userId,
        createdAt,
        title,
        content,
        visitCount,
        likeCount,
        hateCount,
        replyCount,
        postMedias,
        tags);
  }

}

