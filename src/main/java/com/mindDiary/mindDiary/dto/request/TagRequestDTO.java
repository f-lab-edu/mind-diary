package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDTO {

  private String name;

  public Tag createEntity() {
       return Tag.builder()
           .name(name)
           .build();
  }
}
