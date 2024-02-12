package com.maperz.githubrepos.dto.github;

public record Branch(
        String name,
        Commit commit
) {
    public String getLastCommitSha() {
        return commit.sha();
    }

}
