package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.FeelingStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryResponseDTO {

  private int id;

  private LocalDateTime createdAt;

  private FeelingStatus feelingStatus;

  private String title;

  private String content;

  public static DiaryResponseDTO create(int id, LocalDateTime createdAt, FeelingStatus feelingStatus, String title, String content) {
    DiaryResponseDTO diaryResponseDTO = new DiaryResponseDTO();
    diaryResponseDTO.setId(id);
    diaryResponseDTO.setCreatedAt(createdAt);
    diaryResponseDTO.setFeelingStatus(feelingStatus);
    diaryResponseDTO.setTitle(title);
    diaryResponseDTO.setContent(content);
    return diaryResponseDTO;
  }
}
