package com.maperz.githubrepos.service.impl;

import com.maperz.githubrepos.dto.github.Branch;
import com.maperz.githubrepos.dto.github.Repo;
import com.maperz.githubrepos.dto.userrepo.BranchResponse;
import com.maperz.githubrepos.dto.userrepo.UserRepository;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import com.maperz.githubrepos.service.UserRepositoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRepositoriesServiceImpl implements UserRepositoriesService {

    private final RestClient restClient;

    @Override
    public List<UserRepository> getUserRepositories(String username) {
        return getRepos(username).stream()
                .map(repo -> new UserRepository(repo.name(), repo.getOwnerLogin(), getBranches(repo)))
                .toList();
    }
    private List<Repo> getRepos(String username) {
        List<Repo> repoList =
                restClient.get()
                .uri("/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        (request, response) -> {
                            throw new GithubUserNotFoundException("User " + username + " not found on Github");
                        })
                .body(new ParameterizedTypeReference<List<Repo>>(){});
        return Optional.ofNullable(repoList).map(repos -> repos.stream()
                .filter(repo -> !repo.fork())
                .toList()).orElseThrow(() -> new GithubUserNotFoundException("User" + username + "not found"));
    }

    private List<BranchResponse> getBranches(Repo repo) {
        List<Branch> rawBranches = restClient.get()
                .uri("/repos/{owner}/{repo}/branches", repo.getOwnerLogin(), repo.name())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Branch>>() {});
            return Optional.ofNullable(rawBranches).map(branches -> branches.stream()
                    .map(branch -> new BranchResponse(branch.name(), branch.getLastCommitSha()))
                    .toList()).orElse(List.of());
    }
}
