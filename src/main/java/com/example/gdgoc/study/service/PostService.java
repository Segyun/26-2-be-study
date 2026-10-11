package com.example.gdgoc.study.service;

import com.example.gdgoc.study.domain.Post;
import com.example.gdgoc.study.domain.PostNotFoundException;
import com.example.gdgoc.study.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Post createPost(String title, String content) {
    Post post = new Post(postRepository.nextId(), title, content);
    postRepository.save(post);
    return post;
  }

  public List<Post> getPosts() {
    return postRepository.findAll();
  }

  public Post getPost(int id) {
    return findPost(id);
  }

  public Post updatePost(int id, String title, String content) {
    Post existingPost = findPost(id);
    Post post = new Post(existingPost.id(), title, content);
    postRepository.save(post);
    return post;
  }

  public void deletePost(int id) {
    postRepository.delete(findPost(id));
  }

  private Post findPost(int id) {
    Post post = postRepository.findById(id);
    if (post == null) {
      throw new PostNotFoundException(id);
    }
    return post;
  }
}
