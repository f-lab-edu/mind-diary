package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Diary {

  private int id;

  private int userId;

  private LocalDateTime createdAt;

  private FeelingStatus feelingStatus;

  private String title;

  private String content;

  public Diary(int id, int userId, LocalDateTime createdAt, FeelingStatus feelingStatus, String title) {
    this.id = id;
    this.userId = userId;
    this.createdAt = createdAt;
    this.feelingStatus = feelingStatus;
    this.title = title;
  }


  public Diary(int userId, LocalDateTime createdAt, FeelingStatus feelingStatus,  String title, String content) {
    this.userId = userId;
    this.createdAt = createdAt;
    this.feelingStatus = feelingStatus;
    this.title = title;
    this.content = content;

  }

  public static Diary create(int userId, LocalDateTime createdAt, FeelingStatus feelingStatus,  String title, String content) {
    return new Diary(
        userId,
        createdAt,
        feelingStatus,
        title,
        content
    );
  }

}
