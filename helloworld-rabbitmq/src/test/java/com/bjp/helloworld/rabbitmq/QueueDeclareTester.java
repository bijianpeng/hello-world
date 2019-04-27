package com.bjp.helloworld.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QueueDeclareTester {
    private static final String HOST = "192.168.56.99";

    @Test
    public void testQueueAutoDelete() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();

        //生产者
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, null);
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, true, null);
        channel.queueBind(Constants.QUEUE_NAME, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY);
        channel.basicPublish(Constants.EXCHANGE_NAME, Constants.ROUTING_KEY, false, null, "Hello".getBytes());
        channel.close();

        //消费者
        //消费者断开后才会自动删除队列，交换器没有绑定的队列后也会自动删除
        Channel channel1 = connection.createChannel();
        channel1.basicConsume(Constants.QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("--->" + new String(body));
            }
        });
        Thread.sleep(3000);
        channel1.close();

        connection.close();
    }

    @Test
    public void testQueueExclusive() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();

        //生产者
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, null);
        channel.queueDeclare(Constants.QUEUE_NAME, false, true, true, null);
        channel.queueBind(Constants.QUEUE_NAME, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY);
        channel.basicPublish(Constants.EXCHANGE_NAME, Constants.ROUTING_KEY, false, null, "Hello".getBytes());
        channel.close();

        //消费者
        //使用一个新的连接就是抛异常；只能用原来创建队列的连接
        Connection connection1 = factory.newConnection();
        Channel channel1 = connection1.createChannel();
        channel1.basicConsume(Constants.QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("--->" + new String(body));
            }
        });
        Thread.sleep(3000);
        channel1.close();

        connection.close();
        connection1.close();
    }

    @Test
    public void testPublishMandatory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, null);
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, true, null);
        channel.queueBind(Constants.QUEUE_NAME, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY);

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode:" + replyCode);
                System.out.println("replyText:" + replyText);
                System.out.println("exchange:" + exchange);
                System.out.println("routingKey:" + routingKey);
                System.out.println("msg:" + new String(body));
            }
        });

        //使用错误的路由键
        channel.basicPublish(Constants.EXCHANGE_NAME, "123", true, false, null, "Hello".getBytes());
    }

    @Test
    public void testBakExchange() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> args = new HashMap<>();
        args.put("alternate-exchange", Constants.EXCHANGE_NAME_BAK);
        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, args);
        channel.exchangeDeclare(Constants.EXCHANGE_NAME_BAK, BuiltinExchangeType.FANOUT, false, true, null);
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, true, null);
        channel.queueDeclare(Constants.QUEUE_NAME_BAK, false, false, true, null);
        channel.queueBind(Constants.QUEUE_NAME, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY);
        channel.queueBind(Constants.QUEUE_NAME_BAK, Constants.EXCHANGE_NAME_BAK, Constants.ROUTING_KEY);

        channel.basicPublish(Constants.EXCHANGE_NAME, "123", null, "Hello".getBytes());
    }

    @Test
    public void testDLX() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        args.put("x-expires", 60000);
        args.put("x-dead-letter-exchange", Constants.EXCHANGE_NAME_DEAD);

        channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, null);
        channel.exchangeDeclare(Constants.EXCHANGE_NAME_DEAD, BuiltinExchangeType.FANOUT, false, true, null);
        channel.queueDeclare(Constants.QUEUE_NAME, false, false, true, args);
        channel.queueDeclare(Constants.QUEUE_NAME_DEAD, false, false, true, null);
        channel.queueBind(Constants.QUEUE_NAME, Constants.EXCHANGE_NAME, Constants.ROUTING_KEY);
        channel.queueBind(Constants.QUEUE_NAME_DEAD, Constants.EXCHANGE_NAME_DEAD, Constants.ROUTING_KEY);

        channel.basicPublish(Constants.EXCHANGE_NAME, Constants.ROUTING_KEY, null, "Hello".getBytes());
    }
}
