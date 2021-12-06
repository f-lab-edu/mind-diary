package com.mindDiary.mindDiary.service.post;

import static com.mindDiary.mindDiary.service.post.CreatePostTest.makePost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.exception.businessException.NotFoundPostException;
import com.mindDiary.mindDiary.mapper.PostRepository;
import com.mindDiary.mindDiary.service.PostMediaServiceImpl;
import com.mindDiary.mindDiary.service.PostServiceImpl;
import com.mindDiary.mindDiary.service.PostTagServiceImpl;
import com.mindDiary.mindDiary.service.TagServiceImpl;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadPostTest {
  @InjectMocks
  PostServiceImpl postService;

  @Mock
  PostRepository postRepository;

  @Mock
  PostMediaServiceImpl postMediaService;

  @Mock
  TagServiceImpl tagService;

  @Mock
  PostTagServiceImpl postTagService;


  @Test
  @DisplayName("게시글 조회 성공")
  private void readOneSuccess1() {

    // Arrange
    Post post = makePost(new ArrayList<>(), new ArrayList<>());
    int postId = post.getId();

    doReturn(1)
        .when(postRepository)
        .increaseVisitCount(postId);

    doReturn(post)
        .when(postRepository)
        .findById(postId);

    // Act
    Post result = postService.readPost(postId);

    // Assert
    assertThat(result.getId())
        .isEqualTo(post.getId());
    assertThat(result.getContent())
        .isEqualTo(post.getContent());
    assertThat(result.getTitle())
        .isEqualTo(post.getTitle());
    assertThat(result.getCreatedAt())
        .isEqualTo(post.getCreatedAt());
  }

  @Test
  @DisplayName("게시글 조회 실패 : 게시글의 방문자수 올리는 과정을 실패했을 때")
  private void readOneFail1() {

    // Arrange
    Post post = makePost(new ArrayList<>(), new ArrayList<>());
    int postId = post.getId();

    doReturn(0)
        .when(postRepository)
        .increaseVisitCount(postId);

    // Act, Assert
    assertThatThrownBy(() -> {
      postService.readPost(postId);
    }).isInstanceOf(NotFoundPostException.class);

    verify(postRepository, times(0))
        .findById(postId);

  }
}
