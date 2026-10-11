package com.example.gdgoc.study.domain;

public class PostNotFoundException extends RuntimeException {

  public PostNotFoundException(Long id) {
    super("Post " + id + " not found");
  }
}
