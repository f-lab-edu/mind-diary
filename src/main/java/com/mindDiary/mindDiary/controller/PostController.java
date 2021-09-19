package com.mindDiary.mindDiary.controller;

import com.mindDiary.mindDiary.annotation.LoginCheck;
import com.mindDiary.mindDiary.dto.request.CreatePostRequestDTO;
import com.mindDiary.mindDiary.dto.response.PostMediaResponseDTO;
import com.mindDiary.mindDiary.dto.response.ReadPostResponseDTO;
import com.mindDiary.mindDiary.dto.response.TagResponseDTO;
import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.Role;
import com.mindDiary.mindDiary.entity.Tag;
import com.mindDiary.mindDiary.repository.TagRepository;
import com.mindDiary.mindDiary.service.PostLikeHateService;
import com.mindDiary.mindDiary.service.PostService;
import com.sun.mail.iap.Response;
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
    return new ResponseEntity(createPostResponse(post), HttpStatus.OK);
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


  private ReadPostResponseDTO createPostResponse(Post post) {
    ReadPostResponseDTO response = new ReadPostResponseDTO();
    response.setId(post.getId());
    response.setWriter(post.getWriter());
    response.setCreatedAt(post.getCreatedAt());
    response.setTitle(post.getTitle());
    response.setContent(post.getContent());
    response.setVisitCount(post.getVisitCount());
    response.setLikeCount(post.getLikeCount());
    response.setHateCount(post.getHateCount());
    response.setReplyCount(post.getReplyCount());

    List<PostMediaResponseDTO> postMediaResponses = createMediaResponses(post.getPostMedias());
    List<TagResponseDTO> tagResponses = createTagResponses(post.getTags());

    response.setTags(tagResponses);
    response.setPostMedias(postMediaResponses);
    return response;
  }

  private List<PostMediaResponseDTO> createMediaResponses(List<PostMedia> postMedias) {
    return postMedias.stream()
        .map(postMedia -> createMediaResponse(postMedia))
        .collect(Collectors.toList());
  }

  private PostMediaResponseDTO createMediaResponse(PostMedia postMedia) {
    return new PostMediaResponseDTO(postMedia.getType(), postMedia.getUrl());
  }

  private List<TagResponseDTO> createTagResponses(List<Tag> tags) {
    return tags.stream()
        .map(tag -> createTagResponse(tag))
        .collect(Collectors.toList());
  }

  private TagResponseDTO createTagResponse(Tag tag) {
    return new TagResponseDTO(tag.getName());
  }

  private List<ReadPostResponseDTO> createPostResponses(List<Post> posts) {
    return posts.stream()
        .map(post -> createPostResponse(post))
        .collect(Collectors.toList());
  }
}
