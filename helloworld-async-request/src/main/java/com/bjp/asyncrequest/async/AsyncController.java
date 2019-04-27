package com.bjp.asyncrequest.async;

import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class AsyncController {

    private final TaskExecutor taskExecutor;

    public AsyncController(TaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }

    @GetMapping(value = "/test")
    public CompletableFuture<String> echoHelloWorld2()
    {
        System.out.println("12111111111111111111111111");
        return CompletableFuture.supplyAsync(() ->
        {
            System.out.println("222222222222222222222222222");
            randomDelay();
            System.out.println("3333333333333333333333");
            return "Hello World !!";
        }, taskExecutor);
    }

    private void randomDelay()
    {
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
