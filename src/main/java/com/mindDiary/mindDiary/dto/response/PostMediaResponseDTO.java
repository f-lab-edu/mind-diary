package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostMediaResponseDTO {
  private Type type;
  private String url;

  public static PostMediaResponseDTO create(PostMedia postMedia) {
    return new PostMediaResponseDTO(
        postMedia.getType(),
        postMedia.getUrl());
  }
}
