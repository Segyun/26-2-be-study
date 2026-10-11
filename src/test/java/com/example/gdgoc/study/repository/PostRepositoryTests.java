package com.example.gdgoc.study.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.gdgoc.study.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTests {

  @Autowired private PostRepository postRepository;

  @Test
  void persistsFindsUpdatesAndDeletesPost() {
    Post post = postRepository.save(new Post("제목", "내용"));

    assertTrue(post.getId() > 0);
    assertEquals("제목", postRepository.findById(post.getId()).orElseThrow().getTitle());

    Post loadedPost = postRepository.findById(post.getId()).orElseThrow();
    loadedPost.update("수정 제목", "수정 내용");
    postRepository.save(loadedPost);
    postRepository.flush();

    assertEquals("수정 제목", postRepository.findById(post.getId()).orElseThrow().getTitle());

    postRepository.delete(loadedPost);
    assertTrue(postRepository.findById(post.getId()).isEmpty());
  }
}
