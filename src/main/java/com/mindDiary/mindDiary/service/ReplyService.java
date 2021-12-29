package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Reply;
import java.util.List;

public interface ReplyService {

  void createReply(Reply reply);

  List<Reply> readReplies(int postId);
}
