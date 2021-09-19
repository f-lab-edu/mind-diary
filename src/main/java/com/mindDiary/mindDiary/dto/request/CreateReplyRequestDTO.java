package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Reply;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReplyRequestDTO {
  @NotNull
  private String content;

  public Reply createEntity(int userId, int postId) {
    Reply reply = new Reply();
    reply.setUserId(userId);
    reply.setPostId(postId);
    reply.setContent(content);
    reply.setCreatedAt(LocalDateTime.now());
    return reply;
  }
}
