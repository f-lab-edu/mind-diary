package com.mindDiary.mindDiary.service.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.PostTag;
import com.mindDiary.mindDiary.entity.Tag;
import com.mindDiary.mindDiary.entity.Type;
import com.mindDiary.mindDiary.mapper.PostRepository;
import com.mindDiary.mindDiary.service.PostMediaServiceImpl;
import com.mindDiary.mindDiary.service.PostServiceImpl;
import com.mindDiary.mindDiary.service.PostTagServiceImpl;
import com.mindDiary.mindDiary.service.TagServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreatePostTest {

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

  public static PostTag makePostTag() {
    return makePostTag(makeTag());
  }

  public static PostTag makePostTag(Tag tag) {
    return PostTag.builder()
        .postId(1)
        .tag(tag)
        .id(1)
        .build();
  }

  public static Tag makeTag() {
    return Tag.builder()
        .name("name")
        .id(1)
        .build();
  }

  public static PostMedia makePostMedia() {
    return PostMedia.builder()
        .postId(1)
        .type(Type.IMAGE)
        .url("imageUrl.com")
        .id(1)
        .build();
  }

  public static Post makeCreatePost(List<PostMedia> postMedia) {
    return makePost(postMedia, new ArrayList<>());
  }

  public static Post makePost(List<PostMedia> postMedia, List<PostTag> postTags) {
    return Post.builder()
        .id(1)
        .createdAt(LocalDateTime.now())
        .content("content")
        .userId(1)
        .postMedias(postMedia)
        .postTags(postTags)
        .title("title")
        .hateCount(0)
        .likeCount(0)
        .replyCount(0)
        .visitCount(0)
        .writer("writer")
        .build();
  }

  @Test
  @DisplayName("게시글 생성 성공 : 게시글만 주어졌을 때")
  private void createSuccess1() {

    // Arrange
    List<PostMedia> postMedia = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    Post post = makeCreatePost(postMedia);

    // Act
    postService.createPost(post, tags);

    // Assert
    verify(postRepository, times(1))
        .save(argThat(p -> p.getHateCount() == 0 &&
            p.getReplyCount() == 0 &&
            p.getLikeCount() == 0 &&
            p.getVisitCount() == 0 &&
            p.getWriter().equals(post.getWriter()) &&
            p.getPostMedias().isEmpty() &&
            p.getTitle().equals(post.getTitle()) &&
            p.getContent().equals(post.getContent())));

    assertThat(post.getPostMedias().size())
        .isZero();
  }


  @Test
  @DisplayName("게시글 생성 성공 : 게시글, 태그 데이터 주어졌을 때")
  private void createSuccess2() {

    // Arrange
    List<PostMedia> postMedia = new ArrayList<>();
    Post post = makeCreatePost(postMedia);

    List<Tag> tags = new ArrayList<>();
    Tag tag = makeTag();
    tags.add(tag);
    PostTag postTag = makePostTag(tag);
    List<PostTag> postTags = new ArrayList<>();
    postTags.add(postTag);

    // Act
    postService.createPost(post, tags);

    // Assert
    verify(postRepository, times(1))
        .save(argThat(p -> p.getHateCount() == 0 &&
            p.getReplyCount() == 0 &&
            p.getLikeCount() == 0 &&
            p.getVisitCount() == 0 &&
            p.getWriter().equals(post.getWriter()) &&
            p.getPostMedias().isEmpty() &&
            p.getTitle().equals(post.getTitle()) &&
            p.getContent().equals(post.getContent())));

    verify(postTagService, times(1))
        .save(argThat(pts -> IntStream.range(0, pts.size())
            .allMatch(i -> pts.get(i).getTag().getName()
                .equals(tags.get(i).getName()) &&
                pts.get(i).getTag().getId() == tags.get(i).getId() &&
                pts.get(i).getPostId() == post.getId()
            )));

    verify(postMediaService, times(0))
        .save(post.getPostMedias());

    assertThat(postTags.size())
        .isNotZero();
    assertThat(post.getPostMedias().size())
        .isZero();
  }


  @Test
  @DisplayName("게시글 생성 성공 : 게시글, 미디어 데이터 주어졌을 때")
  private void createSuccess3() {

    // Arrange
    List<PostMedia> postMedia = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    List<PostTag> postTags = new ArrayList<>();
    postMedia.add(makePostMedia());
    Post post = makeCreatePost(postMedia);

    // Act
    postService.createPost(post, tags);

    // Assert
    verify(postRepository, times(1))
        .save(argThat(p -> p.getHateCount() == 0 &&
            p.getReplyCount() == 0 &&
            p.getLikeCount() == 0 &&
            p.getVisitCount() == 0 &&
            p.getWriter().equals(post.getWriter()) &&
            p.getPostMedias().isEmpty() &&
            p.getTitle().equals(post.getTitle()) &&
            p.getContent().equals(post.getContent())));

    verify(postTagService, times(0))
        .save(postTags);

    verify(postMediaService, times(1))
        .save(argThat(pm ->
            IntStream.range(0, pm.size())
                .allMatch(i -> pm.get(i).getUrl().equals(post.getPostMedias().get(i).getUrl()) &&
                    pm.get(i).getType().equals(post.getPostMedias().get(i).getType()) &&
                    pm.get(i).getPostId() == post.getPostMedias().get(i).getPostId())
        ));

    assertThat(postTags.size())
        .isZero();
    assertThat(post.getPostMedias().size())
        .isNotZero();
  }

  @Test
  @DisplayName("게시글 생성 성공 : 게시글, 태그, 미디어 데이터가 주어졌을 때")
  private void createSuccess4() {

    // Arrange
    List<PostMedia> postMedia = new ArrayList<>();
    Post post = makeCreatePost(postMedia);

    List<Tag> tags = new ArrayList<>();
    Tag tag = makeTag();
    tags.add(tag);
    PostTag postTag = makePostTag(tag);
    List<PostTag> postTags = new ArrayList<>();
    postTags.add(postTag);

    // Act
    postService.createPost(post, tags);

    // Assert
    verify(postRepository, times(1))
        .save(argThat(p -> p.getHateCount() == 0 &&
            p.getReplyCount() == 0 &&
            p.getLikeCount() == 0 &&
            p.getVisitCount() == 0 &&
            p.getWriter().equals(post.getWriter()) &&
            p.getPostMedias().isEmpty() &&
            p.getTitle().equals(post.getTitle()) &&
            p.getContent().equals(post.getContent())));

    verify(postTagService, times(1))
        .save(argThat(pts -> IntStream.range(0, pts.size())
            .allMatch(i -> pts.get(i).getTag().getName()
                .equals(tags.get(i).getName()) &&
                pts.get(i).getTag().getId() == tags.get(i).getId() &&
                pts.get(i).getPostId() == post.getId()
            )));

    verify(postMediaService, times(1))
        .save(argThat(pm ->
            IntStream.range(0, pm.size())
                .allMatch(i -> pm.get(i).getUrl().equals(post.getPostMedias().get(i).getUrl()) &&
                    pm.get(i).getType().equals(post.getPostMedias().get(i).getType()) &&
                    pm.get(i).getPostId() == post.getPostMedias().get(i).getPostId())
        ));

    assertThat(postTags.size())
        .isNotZero();
    assertThat(post.getPostMedias().size())
        .isNotZero();
  }

  @Test
  @DisplayName("게시글 생성 실패 : 입력한 태그 이름과 일치하는 태그 데이터가 DB에 없을 때")
  private void createFail1() {

    // Arrange
    List<PostMedia> postMedia = new ArrayList<>();
    Post post = makeCreatePost(postMedia);

    List<Tag> tags = new ArrayList<>();
    Tag tag = makeTag();
    tags.add(tag);
    PostTag postTag = makePostTag(tag);
    List<PostTag> postTags = new ArrayList<>();
    postTags.add(postTag);

    doReturn(null)
        .when(tagService)
        .findByNames(argThat(nameList -> IntStream.range(0, nameList.size())
            .allMatch(i -> nameList.get(i).equals(tags.get(i).getName()))
        ));

    // Act , assert
    assertThatThrownBy(() -> {
      postService.createPost(post, tags);
    }).isInstanceOf(NotMatchedTagException.class);

    verify(postRepository, times(0))
        .save(post);

    verify(postTagService, times(0))
        .save(postTags);
  }

}
