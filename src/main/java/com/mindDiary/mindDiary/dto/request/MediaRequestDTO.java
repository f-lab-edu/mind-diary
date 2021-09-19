package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MediaRequestDTO {
  private Type type;
  private String url;

}
