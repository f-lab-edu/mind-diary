package com.mindDiary.mindDiary.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadPostResponseDTO {
  private int id;
  private String writer;
  private LocalDateTime createdAt;
  private String title;
  private String content;
  private int visitCount;
  private int likeCount;
  private int hateCount;
  private int replyCount;
  private List<PostMediaResponseDTO> postMedias;
  private List<TagResponseDTO> tags;
}
