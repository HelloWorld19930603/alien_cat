package com.aliencat.springboot.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * zookeeper client
 */
public class TestZKClient {
    public static void main(String[] args) throws Exception {
        //构造java zk客户端
        ZooKeeper zk = new ZooKeeper("node-1:2181,node-2:2181", 30000, new Watcher() {
            //这里就是事件通知的回调方法
            public void process(WatchedEvent event) {
                System.out.println(event.getState());
                System.out.println(event.getType());
                System.out.println(event.getPath());
            }
        });

//        zk.create("/myGirls","性感的".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //相当于对节点/myGirls 设置了数据变化的监听  一旦节点数据改变 监听就会触发
        zk.getData("/myGirls", true, null);

        //这里对节点数据进行了修改 那么设置的监听就会触发
        zk.setData("/myGirls", "美丽的".getBytes(), -1);

        zk.setData("/myGirls", "婉约的".getBytes(), -1);

        zk.close();
    }
}
