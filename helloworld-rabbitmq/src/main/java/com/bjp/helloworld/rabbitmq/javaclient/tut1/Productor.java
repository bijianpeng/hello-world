package com.bjp.helloworld.rabbitmq.javaclient.tut1;

import com.bjp.helloworld.rabbitmq.Constants;
import com.rabbitmq.client.*;

public class Productor {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.99");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, false, null);
        //创建交换器
        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //1、最简单的方式发布消息的到队列
        //交换器名称为空时，使用默认的direct交换器，并且将路由键作为队列名称
        channel.basicPublish("", Constants.QUEUE_NAME, null, "Hello,RabbitMQ".getBytes());

        //2、手动绑定交换器和队列
        channel.queueBind(Constants.QUEUE_NAME, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY);
        channel.basicPublish(Constants.EXCHANGE_NAME, Constants.ROUTING_KEY, null, "Hello,RabbitMQ".getBytes());


        Thread.sleep(5000);
        channel.close();
        connection.close();
    }
}
