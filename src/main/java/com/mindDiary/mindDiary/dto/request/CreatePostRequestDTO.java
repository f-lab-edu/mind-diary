package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CreatePostRequestDTO {

  @NotNull
  private String title;
  @NotNull
  private String content;

  List<MediaRequestDTO> media;

  List<TagRequestDTO> tag;

  public Post createEntity(Integer userId) {
    Post post = new Post();
    post.setUserId(userId);
    post.setCreatedAt(LocalDateTime.now());
    post.setTitle(title);
    post.setContent(content);
    post.setVisitCount(0);
    post.setLikeCount(0);
    post.setHateCount(0);
    post.setReplyCount(0);

    List<PostMedia> medias = addPostMedia();
    post.addPostMedias(medias);

    List<Tag> tags = addTags();
    post.addTags(tags);

    return post;
  }

  private List<Tag> addTags() {
    if (tag == null || tag.isEmpty()) {
      return new ArrayList<>();
    }
    return tag.stream()
        .map(t -> Tag.create(t.getName()))
        .collect(Collectors.toList());
  }

  private List<PostMedia> addPostMedia() {
    if (media == null || media.isEmpty()) {
      return new ArrayList<>();
    }

    return media.stream()
        .map(m -> PostMedia.create(m.getType(), m.getUrl()))
        .collect(Collectors.toList());
  }
}
