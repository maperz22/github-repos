# github-repos
This repository contains the latest source code of the GitHubRepoSearch app made for recruitment process

## What does the API do?

App is used to check all user's repositories which are not forks with an instance of:
  1. Repository name.
  2. Repostiory owner's login.
  3. A list of branches with their last commit's sha.

## How to run the application using Docker

Since the app has been uploaded to Docker Hub you can easily pull the container image.

1. Run `docker pull maperz22/github-repo-app:latest` to pull and run the image.
2. In order to run again use command `docker run github-repo-app:latest`

## How to run the application without Docker

1. Run `mvn clean test` in order to test the app before usage.
2. After that run `mvn spring-boot:run` to run the app.

## How to use
CONSIDER USING POSTMAN

Use `http://localhost:8080/api/v1/{$username}/repos` with `application\json` as an `Accept` header to check the repositories.
JSON response will be given. Example:
```
[
    {
        "name": "GitHubRepoSearch",
        "owner": "maperz22",
        "branches": [
            {
                "name": "master",
                "lastCommitSha": "ecb75c1df5945a65c5c298c33dc21110b30cade2"
            }
        ]
    },
    {
    ...
    }
]
```

------
If you use a different `Accept` header, you will be given a `406 HTTP Status Code`.
Then the response will be:
```
{
    "status": 406,
    "message": "Media type ... not acceptable. Please use: application/json"
}
```

-----
If you pass an username that does not exists, you will be given a `404 HTTP Status CODE`.
Then the response will be:
```
{
    "status": 404,
    "message": "User not found"
}
```
