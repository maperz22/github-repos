package com.maperz.githubrepos.aop;

import com.maperz.githubrepos.aop.response.ExceptionResponse;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();


    @Test
    @DisplayName("Should handle GithubUserNotFoundException")
    public void handleGithubUserNotFoundException() {

        GithubUserNotFoundException ex = new GithubUserNotFoundException("User not found");

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGithubUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals("User not found", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle HttpMediaTypeNotAcceptableException")
    public void handleHttpMediaTypeNotAcceptableException() {

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleHttpMediaTypeNotAcceptableException();

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.getBody().status());
        assertEquals("This API supports only 'Accept: application/json' header", response.getBody().message());
    }

}