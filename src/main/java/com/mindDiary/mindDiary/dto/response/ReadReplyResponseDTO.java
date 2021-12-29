package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Reply;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyResponseDTO {
  private String writer;
  private String content;
  private LocalDateTime createdAt;

  public static ReadReplyResponseDTO create(Reply reply) {
    return new ReadReplyResponseDTO(
        reply.getWriter(),
        reply.getContent(),
        reply.getCreatedAt());
  }
}
