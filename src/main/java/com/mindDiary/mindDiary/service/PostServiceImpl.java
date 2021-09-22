package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.PageCriteria;
import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.PostTag;
import com.mindDiary.mindDiary.entity.Tag;
import com.mindDiary.mindDiary.repository.PostRepository;
import java.util.List;
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
  public void createPost(Post post) {
    postRepository.save(post);

    if (!post.getTags().isEmpty()) {
      tagService.save(post.getTags());
      createPostTags(post.getTags(), post.getId());
    }

    if (!post.getPostMedias().isEmpty()) {
      createPostMedias(post.getPostMedias(), post.getId());
    }

  }

  @Override
  public List<Post> readHotPosts(int pageNumber) {
    PageCriteria pageCriteria = new PageCriteria(pageNumber, 5);
    return postRepository.findHotPosts(pageCriteria);
  }

  @Override
  @Transactional
  public Post readPost(int postId) {
    postRepository.increaseVisitCount(postId);
    return postRepository.findById(postId);
  }

  private void createPostTags(List<Tag> tags, int postId) {

    List<PostTag> postTags = tags.stream()
        .map(tag -> new PostTag(postId, tag.getId()))
        .collect(Collectors.toList());

    postTagService.save(postTags);

  }


  private void createPostMedias(List<PostMedia> postMedias, int postId) {

    List<PostMedia> newPostMedia = postMedias.stream()
        .map(postMedia -> new PostMedia(postMedia.getType(), postMedia.getUrl(), postId))
        .collect(Collectors.toList());

    postMediaService.save(newPostMedia);
  }
}
