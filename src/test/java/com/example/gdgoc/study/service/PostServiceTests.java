package com.example.gdgoc.study.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.gdgoc.study.domain.Post;
import com.example.gdgoc.study.domain.PostNotFoundException;
import com.example.gdgoc.study.repository.PostRepositoryImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PostServiceTests {

  private PostRepositoryImpl repository;
  private PostService service;

  @BeforeEach
  void setUp() {
    repository = new PostRepositoryImpl();
    service = new PostService(repository);
  }

  @Test
  void crudPreservesPostFieldsAndUpdateIdentity() {
    assertEquals(List.of(), service.getPosts());

    Post first = service.createPost("첫 제목", "첫 내용");
    Post second = service.createPost("두 번째 제목", "두 번째 내용");

    assertNotEquals(first.id(), second.id());
    assertEquals("첫 제목", first.title());
    assertEquals("첫 내용", first.content());
    assertSame(first, service.getPost(first.id()));
    assertEquals(List.of(first, second), service.getPosts());

    Post updated = service.updatePost(first.id(), "수정 제목", "수정 내용");

    assertEquals(first.id(), updated.id());
    assertEquals("수정 제목", updated.title());
    assertEquals("수정 내용", updated.content());
    assertSame(updated, service.getPost(first.id()));
    assertEquals(List.of(updated, second), service.getPosts());

    service.deletePost(first.id());

    assertEquals(List.of(second), service.getPosts());
    assertThrows(PostNotFoundException.class, () -> service.getPost(first.id()));
    assertThrows(PostNotFoundException.class, () -> service.deletePost(first.id()));
    assertNotEquals(first.id(), service.createPost("새 제목", "새 내용").id());
  }

  @ParameterizedTest
  @ValueSource(strings = {"get", "update", "delete"})
  void missingPostThrowsDomainExceptionWithoutChangingStoredPosts(String operation) {
    Post existing = service.createPost("제목", "내용");
    int missingId = existing.id() + 1;

    PostNotFoundException exception =
        assertThrows(
            PostNotFoundException.class,
            () -> {
              switch (operation) {
                case "get" -> service.getPost(missingId);
                case "update" -> service.updatePost(missingId, "변경 제목", "변경 내용");
                case "delete" -> service.deletePost(missingId);
                default -> throw new IllegalArgumentException(operation);
              }
            });

    assertEquals("Post " + missingId + " not found", exception.getMessage());
    assertEquals(List.of(existing), service.getPosts());
    assertSame(existing, service.getPost(existing.id()));
  }
}
