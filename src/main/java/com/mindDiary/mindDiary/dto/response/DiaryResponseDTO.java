package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Diary;
import com.mindDiary.mindDiary.entity.FeelingStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryResponseDTO {

  private int id;

  private LocalDateTime createdAt;

  private FeelingStatus feelingStatus;

  private String title;

  private String content;

  public static DiaryResponseDTO create(Diary diary) {
    return new DiaryResponseDTO(
        diary.getId(),
        diary.getCreatedAt(),
        diary.getFeelingStatus(),
        diary.getTitle(),
        diary.getContent());
  }
}
