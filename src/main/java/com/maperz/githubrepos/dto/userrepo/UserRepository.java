package com.maperz.githubrepos.dto.userrepo;

import com.maperz.githubrepos.dto.github.Branch;

import java.util.List;


public record UserRepository(
        String name,
        String owner,
        List<BranchResponse> branches
) {
}
