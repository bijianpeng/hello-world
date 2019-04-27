package com.bjp.helloworld.springboot.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadUserData {

    @Bean
    public CommandLineRunner initUserData(UserRepository repository) {
        return args -> {
            log.info("loaded user {}", repository.save(new User("Bilbo", 29)));
            log.info("loaded user {}", repository.save(new User("Frodo", 15)));
        };
    }

}
