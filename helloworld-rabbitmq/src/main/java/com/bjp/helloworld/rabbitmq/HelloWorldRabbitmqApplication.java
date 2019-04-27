package com.bjp.helloworld.rabbitmq;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HelloWorldRabbitmqApplication {

    @Profile("default")
    @Bean
    public CommandLineRunner usage() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                System.out.println("Nothing to run!");
            }
        };
    }

    @Profile("!default")
    @Bean
    public CommandLineRunner tutorial() {
        return new RabbitAmqpTutorialRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldRabbitmqApplication.class, args);
    }
}
