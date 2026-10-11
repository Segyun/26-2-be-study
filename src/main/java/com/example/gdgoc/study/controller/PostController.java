package com.example.gdgoc.study.controller;

import com.example.gdgoc.study.domain.Post;
import com.example.gdgoc.study.dto.CreatePostRequest;
import com.example.gdgoc.study.dto.PostResponse;
import com.example.gdgoc.study.dto.UpdatePostRequest;
import com.example.gdgoc.study.service.PostService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PostResponse createPost(@RequestBody CreatePostRequest request) {
    return toResponse(postService.createPost(request.title(), request.content()));
  }

  @GetMapping
  public List<PostResponse> getPosts() {
    return postService.getPosts().stream().map(this::toResponse).toList();
  }

  @GetMapping("/{id}")
  public PostResponse getPost(@PathVariable int id) {
    return toResponse(postService.getPost(id));
  }

  @PutMapping("/{id}")
  public PostResponse updatePost(@PathVariable int id, @RequestBody UpdatePostRequest request) {
    return toResponse(postService.updatePost(id, request.title(), request.content()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePost(@PathVariable int id) {
    postService.deletePost(id);
  }

  private PostResponse toResponse(Post post) {
    return new PostResponse(post.id(), post.title(), post.content());
  }
}
