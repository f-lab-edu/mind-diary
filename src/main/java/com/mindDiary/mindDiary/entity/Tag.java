package com.mindDiary.mindDiary.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Tag {
  private int id;
  private String name;

  public static Tag create(String name) {
    Tag tag = new Tag();
    tag.setName(name);
    return tag;
  }
}
