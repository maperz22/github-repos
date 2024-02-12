package com.maperz.githubrepos.dto.userrepo;

public record BranchResponse(
        String name,
        String lastCommitSha
) {
}
