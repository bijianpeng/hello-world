package com.bjp.helloworld.rabbitmq.javaclient.tut2;

import com.bjp.helloworld.rabbitmq.Constants;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Productor {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.99");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(Constants.QUEUE_NAME, true, false, false, null);

        //发送10条消息，启动两个消费者，这10条消息会平均分配到两个消费者处理
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", Constants.QUEUE_NAME, null, ("Hello,RabbitMQ-" + (i + 1)).getBytes());
        }

        channel.close();
        connection.close();
    }
}
