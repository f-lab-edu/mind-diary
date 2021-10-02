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
public class Reply {

  private int id;
  private int userId;
  private int postId;
  private String content;
  private LocalDateTime createdAt;
  private String writer;

}
