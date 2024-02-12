package com.maperz.githubrepos.controller;

import com.maperz.githubrepos.dto.userrepo.UserRepository;
import com.maperz.githubrepos.service.UserRepositoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-repositories")
@RequiredArgsConstructor
public class UserRepositoriesController {

    private final UserRepositoriesService userRepositoriesService;

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRepository> getUserRepositories(@PathVariable String username) {
        return userRepositoriesService.getUserRepositories(username);
    }

}
