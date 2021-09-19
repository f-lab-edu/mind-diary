package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Reply;
import com.mindDiary.mindDiary.repository.ReplyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

  private final ReplyRepository replyRepository;

  @Override
  public void createReply(Reply reply) {
    replyRepository.save(reply);
  }
  @Override
  public List<Reply> readReplies(int postId) {
    return replyRepository.findByPostId(postId);
  }
}
