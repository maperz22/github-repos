package com.maperz.githubrepos.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.maperz.githubrepos.dto.userrepo.UserRepository;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import com.maperz.githubrepos.service.UserRepositoriesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@WireMockTest
class UserRepositoriesServiceImplTest {

    private WireMockServer wireMockServer;
    private UserRepositoriesService userRepositoriesService;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().port(8091));
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

        assertEquals(2, userRepositories.size());
        assertEquals("repo1", userRepositories.get(0).name());
        assertEquals("owner1", userRepositories.get(0).owner());
        assertEquals("branch1", userRepositories.get(0).branches().get(0).name());
        assertEquals("sha1", userRepositories.get(0).branches().get(0).lastCommitSha());

        for (UserRepository userRepository : userRepositories) {
            assertNotNull(userRepository.name());
            assertNotNull(userRepository.owner());
            assertNotNull(userRepository.branches());
            assertNotEquals("repo3", userRepository.name());
        }

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