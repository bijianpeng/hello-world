package com.bjp.helloworld.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;

public class RabbitAmqpTutorialRunner implements CommandLineRunner {

    @Value("${tutorial.duration:0}")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Start to run for " + duration + "ms");
        Thread.sleep(duration);
        ctx.close();
    }
}
