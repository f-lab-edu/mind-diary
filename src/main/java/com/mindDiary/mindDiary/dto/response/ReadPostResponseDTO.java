package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.PostTag;
import com.mindDiary.mindDiary.entity.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadPostResponseDTO {
  private int id;
  private String writer;
  private LocalDateTime createdAt;
  private String title;
  private String content;
  private int visitCount;
  private int likeCount;
  private int hateCount;
  private int replyCount;
  private List<PostMediaResponseDTO> postMedias;
  private List<TagResponseDTO> tags;

  public ReadPostResponseDTO(int id, String writer, LocalDateTime createdAt, String title, String content,
      int visitCount, int likeCount, int hateCount, int replyCount,
      List<PostMedia> postMedias, List<PostTag> postTags) {
    this.id = id;
    this.writer = writer;
    this.createdAt = createdAt;
    this.title = title;
    this.content = content;
    this.visitCount = visitCount;
    this.likeCount = likeCount;
    this.hateCount = hateCount;
    this.replyCount = replyCount;

    if (postMedias != null && !postMedias.isEmpty()) {
      this.postMedias = postMedias.stream()
          .map(postMedia -> PostMediaResponseDTO.create(postMedia))
          .collect(Collectors.toList());
    }


    if (postTags != null && !postTags.isEmpty()) {
      this.tags = postTags.stream()
          .map(postTag -> TagResponseDTO.create(postTag.getTag()))
          .collect(Collectors.toList());
    }
  }

  public static ReadPostResponseDTO create(Post post) {
    return new ReadPostResponseDTO(
        post.getId(),
        post.getWriter(),
        post.getCreatedAt(),
        post.getTitle(),
        post.getContent(),
        post.getVisitCount(),
        post.getLikeCount(),
        post.getHateCount(),
        post.getReplyCount(),
        post.getPostMedias(),
        post.getPostTags());
  }
}
