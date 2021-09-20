package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponseDTO {
  private String name;

  public static TagResponseDTO create(Tag tag) {
    return new TagResponseDTO(tag.getName());
  }
}
