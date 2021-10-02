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

  public PostMedia(int id, Type type, String url) {
    this.id = id;
    this.type = type;
    this.url = url;
  }
  public PostMedia(Type type, String url) {
    this.type = type;
    this.url = url;
  }

  public PostMedia(Type type, String url, int postId) {
    this.postId = postId;
    this.type = type;
    this.url = url;
  }

  public static PostMedia create(Type type, String url) {
    return new PostMedia(type, url);
  }

}
