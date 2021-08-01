package com.aliencat.communication.rpc.consumer.zk;

import io.netty.util.CharsetUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class MyZkClient implements Watcher {

    private final static String basePath = "/rpc-base";
    private final static Map<String, Host> serverMap = new HashMap<>();
    private static CuratorFramework client;

    public CuratorFramework getClient() {
        if (client == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.builder()
                    .connectString("47.106.168.17:2181")
                    .sessionTimeoutMs(60000)
                    .connectionTimeoutMs(15000)
                    .retryPolicy(retryPolicy)
                    .build();
            client.start();
            System.out.println("zookeeper session established.");

        }
        return client;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //监听字节的变动事件，并处理
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            getChildren();
        }
        if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            String path = watchedEvent.getPath();
            getChildrenData(path);
        }
    }

    /**
     * 获取子节点列表
     */
    public void getChildren() {
        try {
            List<String> nodes = client.getChildren()
                    .usingWatcher(new MyZkClient()).forPath(basePath);
            System.out.println("服务器节点列表：" + nodes);
            //把上线的服务放入map
            for (String node : nodes) {
                if (!serverMap.containsKey(node)) {
                    Host host = new Host().getHost(node);

                    byte[] bytes = client.getData().usingWatcher(new MyZkClient())
                            .forPath(basePath + "/" + node);
                    if (bytes != null && bytes.length > 0) {
                        String[] data = new String(bytes).split("#");
                        if (data.length == 2) {
                            host.setResponseTime(Long.parseLong(data[0]));
                            host.setLastTime(Long.parseLong(data[1]));
                        } else {
                            host.setResponseTime(0);
                            host.setLastTime(0);
                        }
                    }
                    serverMap.put(node, host);
                }
            }

            //删除下线的服务
            Iterator<String> iterator = serverMap.keySet().iterator();
            while (iterator.hasNext()) {
                if (!nodes.contains(iterator.next())) {
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取服务器节点内容
     *
     * @param path
     */
    public void getChildrenData(String path) {
        try {
//            /rpc-base/127.0.0.1:8901
            byte[] bytes = client.getData().usingWatcher(new MyZkClient()).forPath(path);
            String node = path.substring(path.lastIndexOf("/") + 1);
            Host host = serverMap.get(node);
            if (host != null) {
                String[] data = new String(bytes).split("#");
                if (data.length == 2) {
                    host.setResponseTime(Long.parseLong(data[0]));
                    host.setLastTime(Long.parseLong(data[1]));
                } else {
                    host.setResponseTime(0);
                    host.setLastTime(0);
                }
            }
            System.out.println(String.format("节点：%s,内容变更：%s", node, new String(bytes)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将响应信息上报
     *
     * @param childNode
     * @param time
     */
    public void setResponseTimeToNodeData(String childNode, long time) {
        try {
            //上报【请求耗时|系统时间】rpc-base/127.0.0.1:8901
            client.setData().forPath(basePath + "/" + childNode,
                    (time + "#" + System.currentTimeMillis()).getBytes(CharsetUtil.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前最优的服务器
     *
     * @return
     */
    public String getBestServer() {
        long bestTime = -1;
        String key = null;
        for (Map.Entry<String, Host> entry : serverMap.entrySet()) {
            Host host = entry.getValue();
            if (host.getResponseTime() == 0) {
                key = entry.getKey();
                break;
            }
            long responseTime = host.getResponseTime();
            if (bestTime == -1 || bestTime > responseTime) {
                key = entry.getKey();
                bestTime = responseTime;
            }
        }
        return key;
    }

    public Map<String, Host> getServerMap() {
        if (serverMap.size() == 0) {
            getClient();
            getChildren();
        }
        return serverMap;
    }

    /**
     * 定时任务每5秒执行一次，把最后一次请求超过5秒的服务清0
     *
     * @throws Exception
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void scheduled() throws Exception {
        List<String> nodes = getClient().getChildren().forPath(basePath);
        for (String node : nodes) {
            byte[] bytes = getClient().getData().forPath(basePath + "/" + node);
            //responsetime#lasttime
            String[] data = new String(bytes).split("#");
            if (data.length == 2) {
                if (System.currentTimeMillis() - Long.parseLong(data[1]) > 5000) {
                    getClient().setData().forPath(basePath + "/" + node, "0".getBytes());
                    System.out.println(String.format("---定时任务执行---修改节点：%s, 内容为：%s",
                            node, 0));
                }
            }
        }
    }

}
