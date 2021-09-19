package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.dto.request.CreateReplyRequestDTO;
import com.mindDiary.mindDiary.dto.response.ReadReplyResponseDTO;
import com.mindDiary.mindDiary.entity.Reply;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.service.ReplyService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

  private final ReplyService replyService;

  @PostMapping("/{postId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createReply(@PathVariable @Valid int postId,
      @RequestBody @Valid CreateReplyRequestDTO createReplyRequest,
      Integer userId) {
    Reply reply = createReplyRequest.createEntity(userId, postId);
    replyService.createReply(reply);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/{postId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<ReadReplyResponseDTO> readReply(@PathVariable @Valid int postId) {
    List<Reply> replies = replyService.readReplies(postId);
    return new ResponseEntity(createReplyResponses(replies), HttpStatus.OK);
  }

  private List<ReadReplyResponseDTO> createReplyResponses(List<Reply> replies) {
    return replies.stream()
        .map(reply -> createReplyResponse(reply))
        .collect(Collectors.toList());
  }

  private ReadReplyResponseDTO createReplyResponse(Reply reply) {
    return new ReadReplyResponseDTO(reply.getWriter(), reply.getContent(), reply.getCreatedAt());
  }
}
