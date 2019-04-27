package com.bjp.helloworld;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BppOfflineChecker {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DelayQueue<Server> queue = new DelayQueue<Server>();
    private static final String PATH = "/ultrabpp/rmiservice/com.ultrapower.distribfx.basesn.server.WfTypeEntryCountServer";
    private static final String SMSFILE = "/export/home/Project/ShellScript/process_monitor_sms/zkchecker.txt";
    private static String sms;

    public static void main(String[] args) throws Exception {
        String zkConnStr = args[0];
        sms = args[1];

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(args[0])
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.getConnectionStateListenable().addListener(((c, newState) -> {
            logger("state change =>" + newState);
        }));
        client.start();

        if (client.checkExists().forPath(PATH) == null) {
            logger(PATH + " is not exists.");
            return;
        }

        client.getChildren().watched().forPath(PATH);

        //cacheData 为false时无法获取到节点变更后的值
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();

        //监听子节点
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                ChildData data = pathChildrenCacheEvent.getData();
                switch (pathChildrenCacheEvent.getType()) {
                    case CHILD_ADDED:
                        if (cache.getCurrentData().size() > 0) {
                            synchronized (this) {
                                //按节点值分组统计数量,计算重复注册数
                                Map<String, List<ChildData>> serverCount = cache.getCurrentData().stream().collect(Collectors.groupingBy((Function<ChildData, String>) node -> {
                                    return new String(node.getData());
                                }, Collectors.toList()));
                                StringBuilder smsBuilder = new StringBuilder();
                                for (String server : serverCount.keySet()) {
                                    if (serverCount.get(server).size() > 1) {
                                        smsBuilder.append(server + "-" + serverCount.get(server).size() + ";");
                                        logger("duplicate:" + server + ",size:" + serverCount.get(server).size());
                                    }
                                }
                                if (smsBuilder.length() > 0) {
                                    sendSms("BPP ERROR:" + smsBuilder.toString());
                                }
                            }
                        }
                        break;
                    case CHILD_REMOVED:
                        Server server = new Server(new String(data.getData()), System.currentTimeMillis() + 180 * 1000);
                        synchronized (queue) {
                            logger("add queue:" + server);
                            queue.add(server);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //队列中取出过期的元素
        while (true) {
            if (queue.size() > 0) {
                Server server = queue.take();
                String name = server.getName();
                logger("check:" + name);
                long count = cache.getCurrentData().stream().filter(childData -> name.equals(new String(childData.getData()))).count();
                if (count == 0) {
                    logger("offline:" + name);
                    sendSms("BPP OFFLINE:" + name);
                } else {
                    logger("online:" + name);
                }
            } else {
                Thread.sleep(60 * 1000);
            }
        }
    }

    private static void logger(String string) {
        System.out.println(formatter.format(new Date()) + " " + string);
    }

    private static void sendSms(String string) {
        if (!"true".equals(sms)) {
            return;
        }

        try {
            Process proc = Runtime.getRuntime().exec("sh");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes("echo '" + string + "' >> " + SMSFILE + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            proc.waitFor();
            proc.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Server implements Delayed {
        private String name;
        private long expireTime;

        public Server(String name, long expireTime) {
            this.name = name;
            this.expireTime = expireTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return expireTime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.valueOf(this.getDelay(null)).compareTo(Long.valueOf(o.getDelay(null)));
        }

        @Override
        public String toString() {
            return "Server{" +
                    "name='" + name + '\'' +
                    ", expireTime=" + expireTime +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
