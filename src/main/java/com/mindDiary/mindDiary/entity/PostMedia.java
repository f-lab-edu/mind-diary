package com.mindDiary.mindDiary.entity;

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
public class PostMedia {

  private int id;
  private int postId;
  private Type type;
  private String url;


  public PostMedia createWithPostId(int postId) {
    return PostMedia.builder()
        .type(type)
        .url(url)
        .postId(postId)
        .build();
  }

}
