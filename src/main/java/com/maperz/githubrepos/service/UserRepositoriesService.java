package com.maperz.githubrepos.service;

import com.maperz.githubrepos.dto.userrepo.UserRepository;

import java.util.List;

public interface UserRepositoriesService {

    List<UserRepository> getUserRepositories(String username);

}
