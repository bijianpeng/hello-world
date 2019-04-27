package com.bjp.helloworld.rabbitmq.springamqp.tut2;

import com.bjp.helloworld.rabbitmq.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

import java.util.Random;

@RabbitListener(queues = Constants.QUEUE_NAME)
public class Consumer {

    private final int instance;

    public Consumer(int instance) {
        System.out.println("init Consumer:" + instance);
        this.instance = instance;
    }

    @RabbitHandler
    public void receive(String message) {
        this.doWork(message);
    }

    @RabbitHandler
    public void receive(byte[] message) {
        this.doWork(new String(message));
    }

    private void doWork(String str) {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("Consumer[" + instance + "] receive message:" + str);
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(5) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        System.out.println("Consumer[" + instance + "] done:" + watch.getTotalTimeMillis());
    }
}
