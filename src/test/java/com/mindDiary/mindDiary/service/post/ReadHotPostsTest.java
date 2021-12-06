package com.mindDiary.mindDiary.service.post;

import static com.mindDiary.mindDiary.service.post.CreatePostTest.makePost;
import static com.mindDiary.mindDiary.service.post.CreatePostTest.makePostMedia;
import static com.mindDiary.mindDiary.service.post.CreatePostTest.makePostTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.PageCriteria;
import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.PostTag;
import com.mindDiary.mindDiary.mapper.PostRepository;
import com.mindDiary.mindDiary.service.PostMediaServiceImpl;
import com.mindDiary.mindDiary.service.PostServiceImpl;
import com.mindDiary.mindDiary.service.PostTagServiceImpl;
import com.mindDiary.mindDiary.service.TagServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadHotPostsTest {

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
  @DisplayName("페이지별 인기 게시글 목록 조회 성공")
  private void readHotSuccess1() {

    // Arrange
    int pageNumber = 1;
    List<Post> posts = new ArrayList<>();
    List<PostMedia> postMedias = new ArrayList<>();
    List<PostTag> postTags = new ArrayList<>();
    Post post = makePost(postMedias, postTags);
    posts.add(post);

    doReturn(posts)
        .when(postRepository)
        .findHotPosts(argThat(p -> p.getPageNumber() == pageNumber));

    // Act
    List<Post> results = postService.readHotPosts(pageNumber);

    // Assert
    assertThat(results.size())
        .isEqualTo(posts.size());
    assertThat(results.get(0).getId())
        .isEqualTo(posts.get(0).getId());
    assertThat(results.get(0).getContent())
        .isEqualTo(posts.get(0).getContent());
    assertThat(results.get(0).getTitle())
        .isEqualTo(posts.get(0).getTitle());
    assertThat(results.get(0).getCreatedAt())
        .isEqualTo(posts.get(0).getCreatedAt());
  }

  @Test
  @DisplayName("페이지별 인기 게시글 목록 조회 성공 : 게시글에 태그 데이터도 같이 있는 경우")
  private void readHotSuccess2() {
    // Arrange
    int pageNumber = 1;
    List<PostMedia> postMedias = new ArrayList<>();

    PostTag postTag = makePostTag();
    List<PostTag> postTags = new ArrayList<>();
    postTags.add(postTag);

    List<Post> posts = new ArrayList<>();
    Post post = makePost(postMedias, postTags);
    posts.add(post);

    doReturn(posts)
        .when(postRepository)
        .findHotPosts(argThat(p -> p.getPageNumber() == pageNumber));


    doReturn(null)
        .when(postMediaService)
        .findAllByPostIds(argThat(postIdList ->
            IntStream.range(0, postIdList.size())
                .allMatch(i -> postIdList.get(i) == posts.get(i).getId())));

    doReturn(postTags)
        .when(postTagService)
        .findAllByPostIds(argThat(postIdList ->
            IntStream.range(0, postIdList.size())
                .allMatch(i -> postIdList.get(i) == posts.get(i).getId())));

    // Act
    List<Post> results = postService.readHotPosts(pageNumber);

    // Assert
    assertThat(results.size())
        .isEqualTo(posts.size());
    assertThat(results.get(0).getId())
        .isEqualTo(posts.get(0).getId());
    assertThat(results.get(0).getContent())
        .isEqualTo(posts.get(0).getContent());
    assertThat(results.get(0).getTitle())
        .isEqualTo(posts.get(0).getTitle());
    assertThat(results.get(0).getCreatedAt())
        .isEqualTo(posts.get(0).getCreatedAt());

    assertThat(results.get(0).getPostTags().get(0).getId())
        .isEqualTo(posts.get(0).getPostTags().get(0).getId());
    assertThat(results.get(0).getPostTags().get(0).getPostId())
        .isEqualTo(posts.get(0).getPostTags().get(0).getPostId());
    assertThat(results.get(0).getPostTags().get(0).getTag().getId())
        .isEqualTo(posts.get(0).getPostTags().get(0).getTag().getId());
  }

  @Test
  @DisplayName("페이지별 인기 게시글 목록 조회 성공 : 게시글데 미디어 테이터도 같이 있는 경우")
  private void readHotSuccess3() {

    // Arrange
    int pageNumber = 1;

    List<PostTag> postTags = new ArrayList<>();
    List<PostMedia> postMedias = new ArrayList<>();
    postMedias.add(makePostMedia());

    List<Post> posts = new ArrayList<>();
    Post post = makePost(postMedias, postTags);
    posts.add(post);

    doReturn(posts)
        .when(postRepository)
        .findHotPosts(argThat(p -> p.getPageNumber() == pageNumber));

    doReturn(postMedias)
        .when(postMediaService)
        .findAllByPostIds(argThat(postIdList ->
            IntStream.range(0, postIdList.size())
                .allMatch(i -> postIdList.get(i) == posts.get(i).getId())));

    doReturn(null)
        .when(postTagService)
        .findAllByPostIds(argThat(postIdList ->
            IntStream.range(0, postIdList.size())
                .allMatch(i -> postIdList.get(i) == posts.get(i).getId())));

    // Act
    List<Post> results = postService.readHotPosts(pageNumber);

    // Assert
    assertThat(results.size())
        .isEqualTo(posts.size());
    assertThat(results.get(0).getId())
        .isEqualTo(posts.get(0).getId());
    assertThat(results.get(0).getContent())
        .isEqualTo(posts.get(0).getContent());
    assertThat(results.get(0).getTitle())
        .isEqualTo(posts.get(0).getTitle());
    assertThat(results.get(0).getCreatedAt())
        .isEqualTo(posts.get(0).getCreatedAt());

    assertThat(results.get(0).getPostMedias().get(0).getId())
        .isEqualTo(posts.get(0).getPostMedias().get(0).getId());
    assertThat(results.get(0).getPostMedias().get(0).getPostId())
        .isEqualTo(posts.get(0).getPostTags().get(0).getPostId());
    assertThat(results.get(0).getPostMedias().get(0).getUrl())
        .isEqualTo(posts.get(0).getPostMedias().get(0).getUrl());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, -5})
  @DisplayName("페이지별 인기 게시글 목록 조회 실패 : 페이지 숫자가 음수나 0인 경우")
  private void readHotFail1(int pageNumber) {

    // Act, Assert
    assertThatThrownBy(() -> {
      postService.readHotPosts(pageNumber);
    }).isInstanceOf(InvalidPageNumberException.class);

    verify(postRepository, times(0))
        .findHotPosts(any(PageCriteria.class));
  }
}
