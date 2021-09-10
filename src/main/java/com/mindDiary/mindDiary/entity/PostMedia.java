package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PostMedia {

  private int id;
  private int postId;
  private Type type;
  private String url;


  public static PostMedia create(Type type, String url) {
    PostMedia postMedia = new PostMedia();
    postMedia.setType(type);
    postMedia.setUrl(url);
    return postMedia;
  }
}
