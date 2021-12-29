package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.PageCriteria;
import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.PostTag;
import com.mindDiary.mindDiary.entity.Tag;
import com.mindDiary.mindDiary.exception.businessException.NotFoundPostException;
import com.mindDiary.mindDiary.exception.businessException.NotFoundTagException;
import com.mindDiary.mindDiary.mapper.PostRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostMediaService postMediaService;
  private final TagService tagService;
  private final PostTagService postTagService;

  @Override
  @Transactional
  public void createPost(Post post, List<Tag> tags) {

    postRepository.save(post);

    if (!tags.isEmpty()) {
      tagService.save(tags);
      List<String> tagNames = toTagNames(tags);
      List<Tag> newTag = tagService.findByNames(tagNames);
      if(newTag == null || newTag.isEmpty()) {
        throw new NotFoundTagException();
      }
      createPostTags(newTag, post.getId());
    }

    if (!post.getPostMedias().isEmpty()) {
      createPostMedias(post.getPostMedias(), post.getId());
    }


  }



  @Override
  public List<Post> readHotPosts(int pageNumber) {

    PageCriteria pageCriteria = new PageCriteria(pageNumber);
    List<Post> posts = postRepository.findHotPosts(pageCriteria);
    List<Integer> postIds = toPostIds(posts);

    Map<Integer, List<PostMedia>> postMediaMap = findPostMediaMap(postIds);
    Map<Integer, List<PostTag>> postTagMap = findPostTagMap(postIds);

    return posts.stream()
        .map(post -> post.withMediaAndTags(
            postMediaMap.get(post.getId()),
            postTagMap.get(post.getId())))
        .collect(Collectors.toList());
  }

  private Map<Integer, List<PostTag>> findPostTagMap(List<Integer> postIds) {
    return postTagService
        .findAllByPostIds(postIds)
        .stream()
        .collect(Collectors.groupingBy(PostTag::getPostId));
  }


  private Map<Integer, List<PostMedia>> findPostMediaMap(List<Integer> postIds) {
    return postMediaService
        .findAllByPostIds(postIds)
        .stream()
        .collect(Collectors.groupingBy(PostMedia::getPostId));
  }

  private List<Integer> toPostIds(List<Post> posts) {
    return posts.stream()
        .map(p -> p.getId())
        .collect(Collectors.toList());
  }

  private List<String> toTagNames(List<Tag> tags) {
    return tags.stream()
        .map(tag -> tag.getName())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Post readPost(int postId) {
    int res = postRepository.increaseVisitCount(postId);

    if (res <= 0) {
      throw new NotFoundPostException();
    }
    return postRepository.findById(postId);
  }

  private void createPostTags(List<Tag> tags, int postId) {
    List<PostTag> postTags = tags.stream()
        .map(tag -> createPostTag(postId, tag))
        .collect(Collectors.toList());
    postTagService.save(postTags);

  }

  private PostTag createPostTag(int postId, Tag tag) {
    return PostTag.builder()
        .postId(postId)
        .tag(tag)
        .build();
  }

  private void createPostMedias(List<PostMedia> postMedias, int postId) {

    List<PostMedia> newPostMedia = postMedias.stream()
        .map(postMedia -> postMedia.createWithPostId(postId))
        .collect(Collectors.toList());

    postMediaService.save(newPostMedia);
  }
}
