package com.bjp.helloworld.rabbitmq.javaclient.tut2;

import com.bjp.helloworld.rabbitmq.Constants;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.99");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //在消息确认前不再分配新的消息到当前消费者
        channel.basicQos(1);

        channel.basicConsume(Constants.QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("receive message:" + new String(body));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--------------------");
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
