package com.example.gdgoc.study.domain;

public class PostNotFoundException extends RuntimeException {

  public PostNotFoundException(int id) {
    super("Post " + id + " not found");
  }
}
