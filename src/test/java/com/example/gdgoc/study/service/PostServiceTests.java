package com.example.gdgoc.study.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.gdgoc.study.domain.InvalidPostException;
import com.example.gdgoc.study.domain.Post;
import com.example.gdgoc.study.domain.PostNotFoundException;
import com.example.gdgoc.study.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTests {

  @Mock private PostRepository postRepository;

  private PostService service;

  @BeforeEach
  void setUp() {
    service = new PostService(postRepository);
  }

  @Test
  void createPostSavesAndReturnsPost() {
    Post savedPost = new Post("제목", "내용");
    when(postRepository.save(any(Post.class))).thenReturn(savedPost);

    Post result = service.createPost("제목", "내용");

    assertSame(savedPost, result);
    assertEquals("제목", result.getTitle());
    assertEquals("내용", result.getContent());
    verify(postRepository).save(any(Post.class));
  }

  @Test
  void createPostRejectsBlankFieldsBeforeSaving() {
    assertThrows(InvalidPostException.class, () -> service.createPost(" ", "내용"));
    assertThrows(InvalidPostException.class, () -> service.createPost("제목", null));
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void getPostsReturnsRepositoryResults() {
    List<Post> posts = List.of(new Post("제목", "내용"));
    when(postRepository.findAll()).thenReturn(posts);

    assertSame(posts, service.getPosts());
  }

  @Test
  void updatePostMutatesAndSavesExistingPost() {
    Post existingPost = new Post("이전 제목", "이전 내용");
    when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
    when(postRepository.save(existingPost)).thenReturn(existingPost);

    Post updatedPost = service.updatePost(1L, "수정 제목", "수정 내용");

    assertSame(existingPost, updatedPost);
    assertEquals("수정 제목", updatedPost.getTitle());
    assertEquals("수정 내용", updatedPost.getContent());
    verify(postRepository).save(existingPost);
  }

  @Test
  void updatePostRejectsBlankFieldsWithoutMutatingOrSaving() {
    Post existingPost = new Post("이전 제목", "이전 내용");
    when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

    assertThrows(InvalidPostException.class, () -> service.updatePost(1L, "제목", "  "));

    assertEquals("이전 제목", existingPost.getTitle());
    assertEquals("이전 내용", existingPost.getContent());
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void missingPostThrowsDomainException() {
    when(postRepository.findById(42L)).thenReturn(Optional.empty());

    PostNotFoundException exception =
        assertThrows(PostNotFoundException.class, () -> service.getPost(42L));

    assertEquals("Post 42 not found", exception.getMessage());
  }
}
