package com.mindDiary.mindDiary.dto.response;

import com.mindDiary.mindDiary.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMediaResponseDTO {
  private Type type;
  private String url;

}
