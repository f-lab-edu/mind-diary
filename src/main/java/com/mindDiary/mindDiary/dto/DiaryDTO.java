package com.mindDiary.mindDiary.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DiaryDTO {

  private int id;

  private int userId;

  private LocalDateTime createdAt;

  @NotNull
  private FeelingStatus feelingStatus;

  @NotNull
  private String title;

  @NotNull
  private String content;

  public enum FeelingStatus {
    VERY_GOOD, GOOD, NORMAL, BAD, TOTALLY_BAD;
  }
}
