package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
