package com.bjp.helloworld;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TreeCacheTester {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.56.99:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();

        String path = "/hello-world";
        if (client.checkExists().forPath(path) == null) {
            client.create().forPath(path);
        }

        TreeCache cache = new TreeCache(client, path);
        cache.start();

        cache.getListenable().addListener((c, event) -> {
            if (event.getData() != null) {
                System.out.println("type=" + event.getType() + " path=" + event.getData().getPath());
            } else {
                System.out.println("type=" + event.getType());
            }
        });

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.readLine();
    }
}
