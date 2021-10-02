package com.mindDiary.mindDiary.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
  private int id;
  private String name;

  public Tag(String name) {
    this.name = name;
  }

  public static Tag create(String name) {
    return new Tag(name);
  }
}
