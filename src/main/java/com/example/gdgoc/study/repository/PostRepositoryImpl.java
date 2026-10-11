package com.example.gdgoc.study.repository;

import com.example.gdgoc.study.domain.Post;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/** In-memory repository implementation. */
@Repository
public class PostRepositoryImpl implements PostRepository {

  private final List<Post> posts = new ArrayList<>();
  private int nextPostId = 1;

  @Override
  public int nextId() {
    return nextPostId++;
  }

  @Override
  public Post findById(int id) {
    return posts.stream().filter(post -> post.id() == id).findFirst().orElse(null);
  }

  @Override
  public List<Post> findAll() {
    return new ArrayList<>(posts);
  }

  @Override
  public void save(Post post) {
    for (int index = 0; index < posts.size(); index++) {
      if (posts.get(index).id() == post.id()) {
        posts.set(index, post);
        return;
      }
    }
    posts.add(post);
  }

  @Override
  public void delete(Post post) {
    posts.removeIf(existingPost -> existingPost.id() == post.id());
  }
}
