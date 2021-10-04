package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
  private List<PostTag> postTags;

  public Post withMediaAndTags(List<PostMedia> postMediaList, List<PostTag> postTagList) {

    return Post.builder()
        .id(id)
        .userId(userId)
        .writer(writer)
        .createdAt(createdAt)
        .title(title)
        .content(content)
        .visitCount(visitCount)
        .likeCount(likeCount)
        .hateCount(hateCount)
        .replyCount(replyCount)
        .postMedias(postMediaList)
        .postTags(postTagList)
        .build();
  }
}

