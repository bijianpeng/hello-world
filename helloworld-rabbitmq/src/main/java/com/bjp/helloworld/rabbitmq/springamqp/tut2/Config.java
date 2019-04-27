package com.bjp.helloworld.rabbitmq.springamqp.tut2;

import com.bjp.helloworld.rabbitmq.Constants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"tut2"})
@Configuration
public class Config {

    @Bean
    public Queue hello() {
        return new Queue(Constants.QUEUE_NAME);
    }

    @Profile("productor")
    @Bean
    public Productor productor() {
        System.out.println("111111");
        return new Productor();
    }

    @Profile("consumer")
    private static class ConsumerConfig {
        @Bean
        public Consumer consumer1() {
            return new Consumer(1);
        }

        @Bean
        public Consumer consumer2() {
            return new Consumer(2);
        }
    }

}
