package com.bjp.asyncrequest.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class GitLookupController {

    @Autowired
    private GitLookupService gitLookupService;

    @GetMapping
    public GitUser[] query() throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<GitUser> page1 = gitLookupService.findUser("PivotalSoftware");
        CompletableFuture<GitUser> page2 = gitLookupService.findUser("CloudFoundry");
        CompletableFuture<GitUser> page3 = gitLookupService.findUser("Spring-Projects");

        CompletableFuture.allOf(page1, page2, page3).join();
        log.info("Elapsed time: " + (System.currentTimeMillis() - start));
        return new GitUser[]{page1.get(), page2.get(), page3.get()};
    }
}
