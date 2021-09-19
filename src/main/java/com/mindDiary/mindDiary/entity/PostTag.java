package com.mindDiary.mindDiary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class PostTag {

  private int postId;

  private int tagId;

}
