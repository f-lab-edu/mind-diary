package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostTag {

  private int id;
  private int postId;
  private Tag tag;

  public PostTag(int postId, Tag tag) {
    this.postId = postId;
    this.tag = tag;
  }
}
