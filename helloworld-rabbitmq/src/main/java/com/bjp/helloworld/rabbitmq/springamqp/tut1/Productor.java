package com.bjp.helloworld.rabbitmq.springamqp.tut1;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Productor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public Productor() {
        System.out.println("Productor init");
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = "Hello RabbitMQ";
        this.rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println("send message:" + message);
    }
}
