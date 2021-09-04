package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.dto.FeelingStatus;
import com.mindDiary.mindDiary.entity.Diary;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDiaryRequestDTO {
  @NotNull
  private FeelingStatus feelingStatus;

  @NotNull
  private String title;

  @NotNull
  private String content;

  public Diary turnIntoDiaryEntity() {
    return Diary.create(title, content, feelingStatus);
  }
}
