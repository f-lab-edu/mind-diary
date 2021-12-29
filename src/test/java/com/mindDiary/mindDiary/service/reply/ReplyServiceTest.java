package com.mindDiary.mindDiary.service.reply;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Reply;
import com.mindDiary.mindDiary.mapper.ReplyRepository;
import com.mindDiary.mindDiary.service.ReplyServiceImpl;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {

  @InjectMocks
  ReplyServiceImpl replyService;

  @Mock
  ReplyRepository replyRepository;

  public Reply createReply() {
    return Reply.builder()
        .createdAt(LocalDateTime.now())
        .postId(1)
        .content("content")
        .userId(1)
        .id(1)
        .writer("writer")
        .build();
  }

  @Test
  @DisplayName("댓글 달기 성공")
  void createReplySuccess() {
    Reply reply = createReply();

    replyService.createReply(reply);

    verify(replyRepository, times(1))
        .save(argThat(r -> r.getPostId() == reply.getPostId()
        && r.getUserId() == reply.getUserId()));
  }

  @Test
  @DisplayName("게시글 별 댓글 조회")
  void readSuccess() {
    Reply reply = createReply();
    int postId = reply.getPostId();

    replyService.readReplies(postId);

    verify(replyRepository, times(1))
        .findByPostId(postId);
  }

}
