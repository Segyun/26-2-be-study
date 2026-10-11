package com.example.gdgoc.study.domain;

public class InvalidPostException extends RuntimeException {
  public InvalidPostException(String message) {
    super(message);
  }
}
