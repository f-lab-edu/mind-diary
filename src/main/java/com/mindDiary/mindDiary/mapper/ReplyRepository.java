package com.mindDiary.mindDiary.mapper;

import com.mindDiary.mindDiary.entity.Reply;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReplyRepository {

  int save(Reply reply);

  List<Reply> findByPostId(int postId);
}
