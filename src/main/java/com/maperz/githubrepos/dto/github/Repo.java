package com.maperz.githubrepos.dto.github;

public record Repo(
        String name,
        Owner owner,
        boolean fork
) {
    public String getOwnerLogin() {
        return owner.login();
    }
}
