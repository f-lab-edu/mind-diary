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
  private List<PostMedia> postMedias = new ArrayList<>();
  private List<PostTag> postTags = new ArrayList<>();

  public Post(int userId, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount,
      List<PostMedia> postMedias, List<PostTag> postTags) {

    this.userId = userId;
    this.createdAt = createdAt;
    this.title = title;
    this.content = content;
    this.visitCount = visitCount;
    this.likeCount = likeCount;
    this.hateCount = hateCount;
    this.replyCount = replyCount;
    if (postTags != null && !postTags.isEmpty()) {
      this.postTags.addAll(postTags);
    }
    if (postMedias != null && !postMedias.isEmpty()) {
      this.postMedias.addAll(postMedias);
    }
  }

  public Post(int id, int userId, String writer, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount,
      List<PostMedia> postMedias, List<PostTag> postTags) {
    this.id = id;
    this.userId = userId;
    this.writer = writer;
    this.createdAt = createdAt;
    this.title = title;
    this.content = content;
    this.visitCount = visitCount;
    this.likeCount = likeCount;
    this.hateCount = hateCount;
    this.replyCount = replyCount;
    if (postTags != null && !postTags.isEmpty()) {
      this.postTags.addAll(postTags);
    }
    if (postMedias != null && !postMedias.isEmpty()) {
      this.postMedias.addAll(postMedias);
    }

  }

  public Post(int id, int userId, String writer, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount) {
    this.id = id;
    this.userId = userId;
    this.writer = writer;
    this.createdAt = createdAt;
    this.title = title;
    this.content = content;
    this.visitCount = visitCount;
    this.likeCount = likeCount;
    this.hateCount = hateCount;
    this.replyCount = replyCount;
  }

  public static Post create(int userId, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount,
      List<PostMedia> postMedias, List<PostTag> postTags) {

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
        postTags);
  }

  public static Post create(Post post, List<PostMedia> postMedia, List<PostTag> postTags) {

    return new Post(
        post.getId(),
        post.getUserId(),
        post.getWriter(),
        post.getCreatedAt(),
        post.getTitle(),
        post.getContent(),
        post.getVisitCount(),
        post.getLikeCount(),
        post.getHateCount(),
        post.getReplyCount(),
        postMedia,
        postTags);
  }
}

