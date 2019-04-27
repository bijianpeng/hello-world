package com.bjp.helloworld.startertest;

import com.bjp.helloworld.starter.annotation.EnableMyConfig;
import com.bjp.helloworld.starter.autoconfiguration.MyBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Author bijianpeng
 * @create 2019-04-27 22:49
 */
@SpringBootApplication
//@EnableMyConfig
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MyBean myBean() {
        return new MyBean("abc", 12);
    }
}
