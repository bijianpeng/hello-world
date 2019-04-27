package com.bjp.helloworld.rabbitmq.springamqp.tut1;

import com.bjp.helloworld.rabbitmq.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = Constants.QUEUE_NAME)
public class Consumer {

    @RabbitHandler
    public void receive(String message) {
        System.out.println("receive message:" + message);
    }
}
