package com.bjp.helloworld;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 监听子节点的变化
 */
public class PathChildrenCacheTester {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.56.99:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.getConnectionStateListenable().addListener(((c, newState) -> {
            System.out.println("state=" + newState);
        }));
        client.start();

        String path = "/hello-world";
        if (client.checkExists().forPath(path) == null) {
            client.create().forPath(path);
        }

        client.getChildren().watched().forPath(path);

        //cacheData 为false时无法获取到节点变更后的值
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start();

        //监听子节点
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                ChildData data = pathChildrenCacheEvent.getData();
                switch (pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED:
                        System.out.println("  add child:" + data.getPath());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("update child:" + data.getPath());
                        System.out.println(data.getPath() + " new data:" + new String(data.getData()));
                        break;
                    case CHILD_REMOVED:
                        System.out.println("remove child:" + data.getPath());
                        break;
                    default:
                        break;
                }
            }
        });

        client.setData().forPath(path, "123".getBytes());
        Thread.sleep(1000);

        client.create().forPath(path + "/c1");
        Thread.sleep(1000);

        client.setData().forPath(path + "/c1", "456".getBytes());
        Thread.sleep(1000);

        client.delete().forPath(path + "/c1");

        Thread.sleep(600000);
    }
}
