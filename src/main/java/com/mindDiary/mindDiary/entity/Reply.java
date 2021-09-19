package com.mindDiary.mindDiary.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reply {

  private int id;
  private int userId;
  private int postId;
  private String content;
  private LocalDateTime createdAt;
  private String writer;
}
