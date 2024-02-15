# github-repos

Github-repos is an application created for the recruitment process, enabling users to quickly search GitHub repositories to find those that are not forks. 
The app displays the repository name, owner's login, and a list of branches with the last commit. 
It's an ideal tool for recruiters and developers who want to quickly assess a candidate's GitHub activity.

## Prerequisites

Before you run the application, make sure the following requirements are met:
- Docker installed (for running with Docker).
- Maven and JDK version 17 or later installed (for running without Docker).

## Installing Docker

If you don't have Docker installed, visit the [official Docker website](https://docs.docker.com/get-docker/) and follow the instructions for your operating system.

## Installing Maven

Instructions for installing Maven can be found on the [official Apache Maven website](https://maven.apache.org/install.html).

## How to run the application using Docker

Since the app has been uploaded to Docker Hub you can easily pull the container image.

1. Run `docker pull maperz22/github-repo-app:latest` to pull and run the image.
2. In order to run again use command `docker run github-repo-app:latest`

## How to run the application without Docker

1. Run `mvn clean test` in order to test the app before usage.
2. After that run `mvn spring-boot:run` to run the app.

## Usage Examples

To check repositories for the user `exampleUser`, use the following URL:

`http://localhost:8080/api/v1/exampleUser/repos`

You will receive a JSON with a list of repositories that are not forks, along with branch names and the latest commits.

```
[
    {
        "name": "exampleRepostitory",
        "owner": "exampleUser",
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
## Error Handling

If you encounter errors, here are some tips on how to deal with them:

- **406 Not Acceptable**: Ensure that the `Accept` header is set to `application/json`.
- **404 Not Found**: Verify that the username provided is correct and that the user has public repositories.

If you use a different `Accept` header, you will be given a `406 HTTP Status Code`.
Then the response will be:

```
{
    "status": 406,
    "message": "Media type ... not acceptable. Please use: application/json"
}
```

If you pass an username that does not exists, you will be given a `404 HTTP Status CODE`.
Then the response will be:

```
{
    "status": 404,
    "message": "User not found"
}
```

## License

This project is licensed under the MIT License. Details can be found in the LICENSE file.

## Contact

If you have any questions about the application send an email to dabrwowski.racing.35@gmail.com.
