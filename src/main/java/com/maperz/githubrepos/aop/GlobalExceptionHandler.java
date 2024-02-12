package com.maperz.githubrepos.aop;

import com.maperz.githubrepos.aop.response.ExceptionResponse;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(GithubUserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleGithubUserNotFoundException(GithubUserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(
                        HttpStatus.NOT_FOUND.value(),
                        e.getMessage())
                );
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMediaTypeNotAcceptableException() {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(
                        HttpStatus.NOT_ACCEPTABLE.value(),
                        "This API supports only 'Accept: application/json' header")
                );
    }

}
