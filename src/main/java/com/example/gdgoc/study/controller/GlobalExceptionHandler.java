package com.example.gdgoc.study.controller;

import com.example.gdgoc.study.domain.InvalidPostException;
import com.example.gdgoc.study.domain.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidPostException.class)
  public ResponseEntity<String> handleInvalidPost(InvalidPostException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(PostNotFoundException.class)
  public ResponseEntity<String> handlePostNotFound(PostNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
