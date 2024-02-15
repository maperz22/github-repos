package com.maperz.githubrepos.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.maperz.githubrepos.dto.userrepo.BranchResponse;
import com.maperz.githubrepos.dto.userrepo.UserRepository;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import com.maperz.githubrepos.service.UserRepositoriesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
class UserRepositoriesServiceImplTest {

    private WireMockServer wireMockServer;
    private UserRepositoriesService userRepositoriesService;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        stubFor(get(urlEqualTo("/users/username/repos"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("user-repos.json")));

        stubFor(get(urlEqualTo("/repos/owner1/repo1/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("branches1.json")));

        stubFor(get(urlEqualTo("/repos/owner2/repo2/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("branches2.json")));

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + wireMockServer.port())
                .build();

        userRepositoriesService = new UserRepositoriesServiceImpl(restClient);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("Should return non-empty list of repositories")
    void getUserRepositories_Success() {

        List<UserRepository> userRepositories = userRepositoriesService.getUserRepositories("username");

        List<UserRepository> expected = List.of(
                new UserRepository("repo1", "owner1", List.of(new BranchResponse("branch1", "sha1"), new BranchResponse("branch2", "sha2"))),
                new UserRepository("repo2", "owner2", List.of(new BranchResponse("branch3", "sha3"), new BranchResponse("branch4", "sha4")))
        );

        assertEquals(expected, userRepositories);

    }

    @Test
    @DisplayName("Should throw GithubUserNotFoundException when user not found")
    void getUserRepositories_UserNotFound() {
        assertThrows(GithubUserNotFoundException.class, () -> userRepositoriesService.getUserRepositories("unknown"));
    }

    @Test
    @DisplayName("Should return empty list of repositories")
    void getUserRepositories_NoRepos() {
        stubFor(get(urlEqualTo("/users/username/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        List<UserRepository> userRepositories = userRepositoriesService.getUserRepositories("username");

        assertEquals(0, userRepositories.size());
    }

    @Test
    @DisplayName("Should return empty list of branches")
    void getUserRepositories_NoBranches() {
        stubFor(get(urlEqualTo("/repos/owner1/repo1/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        List<UserRepository> userRepositories = userRepositoriesService.getUserRepositories("username");

        assertEquals(2, userRepositories.size());
        assertEquals(0, userRepositories.get(0).branches().size());
    }
}