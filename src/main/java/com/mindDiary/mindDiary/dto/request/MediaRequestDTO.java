package com.mindDiary.mindDiary.dto.request;

import com.mindDiary.mindDiary.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MediaRequestDTO {

  private Type type;
  private String url;

}
