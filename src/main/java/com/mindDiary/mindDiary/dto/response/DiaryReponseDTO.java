package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.dto.FeelingStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryReponseDTO {

  private int id;

  private LocalDateTime createdAt;

  private FeelingStatus feelingStatus;

  private String title;

  private String content;

  public static DiaryReponseDTO create(int id, LocalDateTime createdAt, FeelingStatus feelingStatus, String title, String content) {
    DiaryReponseDTO diaryReponseDTO = new DiaryReponseDTO();
    diaryReponseDTO.setId(id);
    diaryReponseDTO.setCreatedAt(createdAt);
    diaryReponseDTO.setFeelingStatus(feelingStatus);
    diaryReponseDTO.setTitle(title);
    diaryReponseDTO.setContent(content);
    return diaryReponseDTO;
  }
}
