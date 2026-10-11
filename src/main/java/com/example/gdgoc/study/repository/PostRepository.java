package com.example.gdgoc.study.repository;

import com.example.gdgoc.study.domain.Post;
import java.util.List;

public interface PostRepository {

  int nextId();

  Post findById(int id);

  List<Post> findAll();

  void save(Post post);

  void delete(Post post);
}
