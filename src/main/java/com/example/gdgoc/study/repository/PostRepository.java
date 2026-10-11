package com.example.gdgoc.study.repository;

import com.example.gdgoc.study.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
