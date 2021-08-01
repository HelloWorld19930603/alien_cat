package com.aliencat.communication.rpc.provider;

import com.aliencat.communication.rpc.provider.server.RpcServer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcProviderApplication implements CommandLineRunner {


    @Autowired
    RpcServer rpcServer;

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.24.124:2181")
                .sessionTimeoutMs(100)
                .connectionTimeoutMs(15000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        int port = 8901;
        new Thread(() -> rpcServer.startServer("127.0.0.1", port)).start();

        //创建服务器节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath("/rpc-base/127.0.0.1:" + port, "".getBytes());

    }
}
