package com.maperz.githubrepos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class RestClientConfiguration {

    @Value("${github.api.url}")
    private String GITHUB_API_URL;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(GITHUB_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


}
