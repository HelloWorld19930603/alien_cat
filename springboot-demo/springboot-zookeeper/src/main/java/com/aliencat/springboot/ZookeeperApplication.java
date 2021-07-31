package com.aliencat.springboot;

import com.aliencat.springboot.client.ZkClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ZookeeperApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ZookeeperApplication.class, args);
        ZkClient zkClient = context.getBean(ZkClient.class);
        zkClient.register();
    }
}
