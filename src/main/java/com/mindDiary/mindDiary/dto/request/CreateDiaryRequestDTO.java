package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Diary;
import com.mindDiary.mindDiary.entity.FeelingStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiaryRequestDTO {

  @NotNull
  private FeelingStatus feelingStatus;

  @NotNull
  private String title;

  @NotNull
  private String content;

  public Diary createEntity(int userId) {

    return Diary.builder()
        .userId(userId)
        .createdAt(LocalDateTime.now())
        .feelingStatus(feelingStatus)
        .title(title)
        .content(content)
        .build();
  }
}
