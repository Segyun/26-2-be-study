package com.example.gdgoc.study.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String content;

  protected Post() {}

  public Post(String title, String content) {
    validate(title, content);
    this.title = title;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public void update(String title, String content) {
    validate(title, content);
    this.title = title;
    this.content = content;
  }

  private static void validate(String title, String content) {
    if (title == null || title.isBlank()) {
      throw new InvalidPostException("title must not be blank");
    }
    if (content == null || content.isBlank()) {
      throw new InvalidPostException("content must not be blank");
    }
  }
}
