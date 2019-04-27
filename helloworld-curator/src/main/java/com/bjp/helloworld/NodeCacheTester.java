package com.bjp.helloworld;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听一个节点值的变化
 */
public class NodeCacheTester {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "192.168.56.99:2181",
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        String path = "/hello-world";
        if (client.checkExists().forPath(path) == null) {
            client.create().forPath(path);
        }

        final NodeCache cache = new NodeCache(client, path, false);
        cache.start();

        //监听节点数值发生变化
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(cache.getCurrentData().getPath() + " data changed:" + new String(cache.getCurrentData().getData()));
            }
        });

        client.setData().forPath(path, "123".getBytes());

        Thread.sleep(60000);
    }
}
