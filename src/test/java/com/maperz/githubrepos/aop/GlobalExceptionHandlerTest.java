package com.maperz.githubrepos.aop;

import com.maperz.githubrepos.aop.response.ExceptionResponse;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();


    @Test
    public void handleGithubUserNotFoundException() {

        GithubUserNotFoundException ex = new GithubUserNotFoundException("User not found");

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleGithubUserNotFoundException(ex);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("User not found", response.getBody().message());
    }

    @Test
    public void handleHttpMediaTypeNotAcceptableException() {

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleHttpMediaTypeNotAcceptableException();

        assertEquals(HttpStatusCode.valueOf(406), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(406, response.getBody().status());
        assertEquals("This API supports only 'Accept: application/json' header", response.getBody().message());
    }

}