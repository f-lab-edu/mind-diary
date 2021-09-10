package com.mindDiary.mindDiary.service;

import com.mindDiary.mindDiary.entity.Post;
import com.mindDiary.mindDiary.entity.PostMedia;
import com.mindDiary.mindDiary.entity.Tag;
import com.mindDiary.mindDiary.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostMediaService postMediaService;
  private final TagService tagService;
  private final PostTagService postTagService;

  @Override
  public void createPost(Post post) {
    postRepository.save(post);

    createPostTags(post.getTags(), post.getId());
    createPostMedias(post.getPostMedias(), post.getId());

  }

  @Override
  public List<Post> readHotPosts() {
    return postRepository.findHotPosts();
  }

  @Override
  public Post readPost(int postId) {
    postRepository.increaseVisitCount(postId);
    return postRepository.findById(postId);
  }

  private void createPostTag(Tag tag, int postId) {
      Tag findTag = tagService.findByName(tag.getName());

      if (findTag != null) {
        postTagService.save(postId, findTag.getId());
        return;
      }

      tagService.save(tag);
      postTagService.save(postId, tag.getId());
  }

  private void createPostTags(List<Tag> tags, int postId) {
    if (tags.isEmpty()) {
      return;
    }

    for (Tag tag : tags) {
      createPostTag(tag, postId);
    }
  }

  private void createPostMedias(List<PostMedia> postMedias, int postId) {
    if (postMedias.isEmpty()) {
      return;
    }
    postMedias.forEach(postMedia -> postMedia.setPostId(postId));
    postMediaService.save(postMedias);
  }
}
