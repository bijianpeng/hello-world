package com.bjp.asyncrequest.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class GitLookupService {
    private static final String API_URL = "https://api.github.com/users/%s";

    private RestTemplate restTemplate;

    public GitLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<GitUser> findUser(String name) throws InterruptedException {
        log.info("loop up user:{}", name);
        String url = String.format(API_URL, name);
        GitUser result = restTemplate.getForObject(url, GitUser.class);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture(result);
    }
}
