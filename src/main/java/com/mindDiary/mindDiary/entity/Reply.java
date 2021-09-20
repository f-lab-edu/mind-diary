package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

  private int id;
  private int userId;
  private int postId;
  private String content;
  private LocalDateTime createdAt;
  private String writer;

  public Reply(int userId, int postId, String content, LocalDateTime createdAt) {
    this.userId = userId;
    this.postId = postId;
    this.content = content;
    this.createdAt = createdAt;
  }

  public static Reply create(int userId, int postId, String content, LocalDateTime createdAt) {
    return new Reply(
        userId,
        postId,
        content,
        createdAt);
  }
}
