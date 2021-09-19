package com.mindDiary.mindDiary.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReadReplyResponseDTO {
  private String writer;
  private String content;
  private LocalDateTime createdAt;
}
