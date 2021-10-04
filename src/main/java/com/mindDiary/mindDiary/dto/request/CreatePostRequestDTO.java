package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDTO {

  @NotNull
  private String title;
  @NotNull
  private String content;

  List<MediaRequestDTO> media;

  List<TagRequestDTO> tag;

  public Post createPostEntity(int userId) {
    return Post.builder()
        .userId(userId)
        .createdAt(LocalDateTime.now())
        .title(title)
        .content(content)
        .postMedias(createPostMediasEntity())
        .build();
  }

  public List<Tag> createTagsEntity() {
    if (tag == null || tag.isEmpty()) {
      return new ArrayList<>();
    }

    return tag.stream()
        .map(t -> t.createEntity())
        .collect(Collectors.toList());
  }

  private List<PostMedia> createPostMediasEntity() {
    if (media == null || media.isEmpty()) {
      return new ArrayList<>();
    }

    return media.stream()
        .map(m -> m.createEntity())
        .collect(Collectors.toList());
  }

}
