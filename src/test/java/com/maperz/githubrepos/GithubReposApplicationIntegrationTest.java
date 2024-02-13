package com.maperz.githubrepos;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class  GithubReposApplicationIntegrationTest {

	@LocalServerPort
	private int port;

	private WebTestClient webTestClient;

	private static WireMockServer wireMockServer;

	@BeforeAll
	static void setup() {
		wireMockServer = new WireMockServer(8089);
		wireMockServer.start();
		configureFor("localhost", wireMockServer.port());

		stubFor(get(urlPathEqualTo("/users/username/repos"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBodyFile("user-repos.json")));

		stubFor(get(urlPathEqualTo("/repos/owner1/repo1/branches"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBodyFile("branches1.json")));

		stubFor(get(urlPathEqualTo("/repos/owner2/repo2/branches"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBodyFile("branches2.json")));

	}

	@BeforeEach
	void setupWebTestClient() {
		webTestClient = WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.build();
	}



	@AfterAll
	static void tearDown() {
		wireMockServer.stop();
	}

	@Test
	@DisplayName("Should get user repositories")
	public void testGetUserRepositories_Success() {
		webTestClient.get().uri("/api/v1/user-repositories/username")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[0].name").isEqualTo("repo1")
				.jsonPath("$[0].owner").isEqualTo("owner1")
				.jsonPath("$[0].branches[0].name").isEqualTo("branch1")
				.jsonPath("$[0].branches[0].lastCommitSha").isEqualTo("sha1")
				.jsonPath("$[0].branches[1].name").isEqualTo("branch2")
				.jsonPath("$[0].branches[1].lastCommitSha").isEqualTo("sha2")
				.jsonPath("$[1].name").isEqualTo("repo2")
				.jsonPath("$[1].owner").isEqualTo("owner2")
				.jsonPath("$[1].branches[0].name").isEqualTo("branch3")
				.jsonPath("$[1].branches[0].lastCommitSha").isEqualTo("sha3")
				.jsonPath("$[1].branches[1].name").isEqualTo("branch4")
				.jsonPath("$[1].branches[1].lastCommitSha").isEqualTo("sha4");
	}

	@Test
	@DisplayName("Should handle GithubUserNotFoundException")
	public void testGetUserRepositories_UserNotFound() {
		webTestClient.get().uri("/api/v1/user-repositories/unknownUser")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	@DisplayName("Should handle HttpMediaTypeNotAcceptableException")
	public void testGetUserRepositories_InvalidAcceptHeader() {
		webTestClient.get().uri("/api/v1/user-repositories/username")
				.header("Accept", "application/xml")
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE.value())
				.expectBody()
				.jsonPath("$.status").isEqualTo(HttpStatus.NOT_ACCEPTABLE.value())
				.jsonPath("$.message").isEqualTo("This API supports only 'Accept: application/json' header");
	}

}
