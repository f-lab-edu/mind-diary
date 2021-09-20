package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.dto.request.CreatePostRequestDTO;
import com.mindDiary.mindDiary.dto.response.ReadPostResponseDTO;
import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.service.PostLikeHateService;
import com.mindDiary.mindDiary.service.PostService;
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
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final PostLikeHateService postLikeHateService;

  @PostMapping
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createPost(@RequestBody @Valid CreatePostRequestDTO createPostRequestDTO, Integer userId) {
    Post post = createPostRequestDTO.createEntity(userId);
    postService.createPost(post);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/hot/{pageNumber}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity<List<ReadPostResponseDTO>> readHotPosts(@PathVariable @Valid int pageNumber) {
    List<Post> posts = postService.readHotPosts(pageNumber);
    return new ResponseEntity<>(createPostResponses(posts), HttpStatus.OK);
  }


  @GetMapping("/{postId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity readPost(@PathVariable @Valid int postId) {

    Post post = postService.readPost(postId);
    return new ResponseEntity(ReadPostResponseDTO.create(post), HttpStatus.OK);
  }

  @PostMapping("/like/{postId}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createPostLike(@PathVariable @Valid int postId, Integer userId) {
    postLikeHateService.createLike(postId, userId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/hate/{postId}/{pageNumber}")
  @LoginCheck(checkLevel = Role.USER)
  public ResponseEntity createPostHate(@PathVariable @Valid int postId, Integer userId) {
    postLikeHateService.createHate(postId, userId);
    return new ResponseEntity(HttpStatus.OK);
  }

  private List<ReadPostResponseDTO> createPostResponses(List<Post> posts) {
    return posts.stream()
        .map(post -> ReadPostResponseDTO.create(post))
        .collect(Collectors.toList());
  }


}
