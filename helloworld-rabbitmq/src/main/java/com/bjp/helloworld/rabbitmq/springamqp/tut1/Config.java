package com.bjp.helloworld.rabbitmq.springamqp.tut1;

import com.bjp.helloworld.rabbitmq.Constants;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"tut1"})
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
    @Bean
    public Consumer consumer() {
        return new Consumer();
    }
}
