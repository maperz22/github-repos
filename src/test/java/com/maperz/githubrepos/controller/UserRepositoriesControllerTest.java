package com.maperz.githubrepos.controller;

import com.maperz.githubrepos.dto.userrepo.UserRepository;
import com.maperz.githubrepos.error.GithubUserNotFoundException;
import com.maperz.githubrepos.service.UserRepositoriesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoriesControllerTest {

    @Mock
    private UserRepositoriesService userRepositoriesService;

    @InjectMocks
    private UserRepositoriesController controller;

    @Test
    @DisplayName("Should return non-empty list of repositories")
    void getUserRepositories_ReturnsNonEmptyList() {
        // Given
        when(userRepositoriesService.getUserRepositories(anyString()))
                .thenReturn(Collections.singletonList(new UserRepository("repoName", "ownerLogin", Collections.emptyList())));

        // When
        List<UserRepository> result = controller.getUserRepositories("username");

        // Then
        assertEquals(1, result.size());
        assertEquals("repoName", result.get(0).name());
    }

    @Test
    @DisplayName("Should throw GithubUserNotFoundException when user not found")
    void getUserRepositories_UserNotFound() {
        // Given
        when(userRepositoriesService.getUserRepositories(anyString()))
                .thenThrow(new GithubUserNotFoundException("User not found"));

        assertThrows(GithubUserNotFoundException.class, () -> {
            controller.getUserRepositories("unknownUser");
        });
    }
}