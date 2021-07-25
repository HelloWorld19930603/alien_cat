package com.aliencat.springboot.service.impl;

import com.aliencat.springboot.service.ZookeeperService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ZooKeeperServiceImpl implements ZookeeperService {


    private static final String lockPath = "/lock/order";
    @Autowired
    private CuratorFramework zkClient;

    @Override
    public void makeOrder(String product) {
        log.info("try do job for " + product);
        String path = lockPath + "/" + product;

        try {
            // InterProcessMutex 构建一个分布式锁
            InterProcessMutex lock = new InterProcessMutex(zkClient, path);
            try {
                if (lock.acquire(5, TimeUnit.HOURS)) {
                    // 模拟业务处理耗时5秒
                    Thread.sleep(5 * 1000);
                    log.info("do job " + product + "done");
                }
            } finally {
                lock.release();  // 释放该锁
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
