package com.bjp.helloworld.kafka;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MyProducer {
    Producer<String, String> producer;

    public MyProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", Constants.KAFKA_SERVERS);

        //指定必须要有多少个partition副本收到消息，生产者才会认为消息的写入是成功的
        //acks=0，生产者不需要等待服务器的响应，以网络能支持的最大速度发送消息，吞吐量高，但是如果broker没有收到消息，生产者是不知道的
        //acks=1，leader partition收到消息，生产者就会收到一个来自服务器的成功响应
        //acks=all，所有的partition都收到消息，生产者才会收到一个服务器的成功响应
        props.put("acks", "all");

        //收到回复的超时时间
//        props.put("delivery.timeout.ms", 30000);

        //发送到同一个partition的消息会被先存储在batch中，该参数指定一个batch可以使用的内存大小，单位是byte。不一定需要等到batch被填满才能发送
//        props.put("batch.size", 16384);

        //生产者在发送消息前等待linger.ms，从而等待更多的消息加入到batch中。如果batch被填满或者linger.ms达到上限，就把batch中的消息发送出去
//        props.put("linger.ms", 1);

        //生产者从服务器收到临时性错误时，生产者重发消息的次数
//        props.put("retries", 3);

        //设置生产者内缓存区域的大小，生产者用它缓冲要发送到服务器的消息
//        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void send(String key, String value) {
        System.out.println("send: " + value);
        producer.send(new ProducerRecord<String, String>(Constants.TOPIC_NAME, key, value));
    }

    public void syncSend(String key, String value){
        try {
            RecordMetadata metadata = producer.send(new ProducerRecord<String, String>(Constants.TOPIC_NAME, key, value)).get();
            System.out.printf("%s : partition = %d, offset = %d%n", value, metadata.partition(), metadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void asyncSend(String key, String value) {
        producer.send(new ProducerRecord<String, String>(Constants.TOPIC_NAME,0, key, value), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                System.out.printf("%s : partition = %d, offset = %d%n", value, metadata.partition(), metadata.offset());
            }
        });
    }

    public void closeProducer() {
        producer.close();
    }

    public static void main(String[] args) {
        MyProducer producer = new MyProducer();
        for (int i = 0; i < Integer.MAX_VALUE; i++){
            producer.asyncSend(null, "value" + i);
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        producer.syncSend(null, "value0");
    }
}
