package com.mindDiary.mindDiary.entity;

import com.mindDiary.mindDiary.dto.FeelingStatus;
import com.mindDiary.mindDiary.dto.response.DiaryReponseDTO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diary {

  private int id;

  private int userId;

  private LocalDateTime createdAt;

  private FeelingStatus feelingStatus;

  private String title;

  private String content;

  public static Diary create(String title, String content, FeelingStatus feelingStatus) {
    Diary diary = new Diary();
    diary.setTitle(title);
    diary.setContent(content);
    diary.setFeelingStatus(feelingStatus);
    return diary;
  }

  public DiaryReponseDTO turnIntoDiaryResponseDTO() {
    return DiaryReponseDTO.create(id, createdAt, feelingStatus, title, content);
  }

}
