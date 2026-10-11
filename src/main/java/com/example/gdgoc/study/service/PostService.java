package com.example.gdgoc.study.service;

import com.example.gdgoc.study.domain.Post;
import com.example.gdgoc.study.domain.PostNotFoundException;
import com.example.gdgoc.study.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Transactional
  public Post createPost(String title, String content) {
    return postRepository.save(new Post(title, content));
  }

  @Transactional(readOnly = true)
  public List<Post> getPosts() {
    return postRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Post getPost(Long id) {
    return findPost(id);
  }

  @Transactional
  public Post updatePost(Long id, String title, String content) {
    Post existingPost = findPost(id);
    existingPost.update(title, content);
    return postRepository.save(existingPost);
  }

  @Transactional
  public void deletePost(Long id) {
    postRepository.delete(findPost(id));
  }

  private Post findPost(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
  }
}
