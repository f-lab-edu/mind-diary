package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Reply;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReplyRequestDTO {

  @NotNull
  private String content;

  public Reply createEntity(int userId, int postId) {

    return Reply.create(
        userId,
        postId,
        content,
        LocalDateTime.now());
  }
}
