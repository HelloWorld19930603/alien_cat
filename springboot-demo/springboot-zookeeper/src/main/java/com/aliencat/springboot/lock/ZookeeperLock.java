package com.aliencat.springboot.lock;

import com.aliencat.springboot.client.ZkClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.Watcher;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ZookeeperLock implements Lock {
    //ZkLock的节点链接
    private static final String ZK_PATH = "/test/lock";
    private static final String LOCK_PREFIX = ZK_PATH + "/";
    private static final long WAIT_TIME = 1000;
    //Zk客户端
    CuratorFramework client = null;
    private String locked_id_path = null;
    private String locked_path = null;
    private String prior_path = null;
    //本地锁
    private ReentrantLock localLock = new ReentrantLock();

    public ZookeeperLock() {
        ZkClient.instance.init();
        synchronized (ZkClient.instance) {
            if (!ZkClient.instance.isNodeExist(ZK_PATH)) {
                ZkClient.instance.createNode(ZK_PATH, null);
            }
        }
        client = ZkClient.instance.getClient();
    }

    /**
     * 分布式锁
     */
    @Override
    public void lock() {
        try {
            //可重入，确保同一线程，可以重复加锁
            localLock.lock();
            //首先尝试着去加锁
            boolean locked = tryLock();
            //如果加锁失败就去等待
            while (!locked) {
                await();
                //获取等待的子节点列表
                List<String> waiters = getWaiters();
                //判断，是否加锁成功
                locked = checkLocked(waiters);
            }
        } catch (Exception e) {
            e.printStackTrace();
            localLock.unlock();
        }
    }

    /**
     * 创建用于获取锁的临时节点
     *
     * @throws Exception
     */
    public String createLocked() {
        //创建临时Znode
        locked_path = ZkClient.instance.createEphemeralSeqNode(LOCK_PREFIX);
        return locked_path;
    }

    /**
     * 尝试加锁
     *
     * @return 是否加锁成功
     * @throws Exception 异常
     */
    public boolean tryLock() {
        while (createLocked() != null) {

        }
        //然后获取所有节点
        List<String> waiters = getWaiters();
        //取得加锁的排队编号
        locked_id_path = getIdPath(locked_path);
        //获取等待的子节点列表，判断自己是否第一个
        if (checkLocked(waiters)) {
            return true;
        }
        // 判断自己排第几个
        int index = Collections.binarySearch(waiters, locked_id_path);
        if (index < 0) { // 网络抖动，获取到的子节点列表里可能已经没有自己了
            return tryLock();
        }
        //如果自己没有获得锁，则要监听前一个节点
        prior_path = ZK_PATH + "/" + waiters.get(index - 1);
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        //TODO
    }

    /**
     * 释放分布式锁
     */
    @Override
    public void unlock() {
        try {
            if (ZkClient.instance.isNodeExist(locked_path)) {
                //删除临时节点
                client.delete().forPath(locked_path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放本地锁
            localLock.unlock();
        }
    }

    @Override
    public Condition newCondition() {
        //TODO
        return null;
    }

    /**
     * 线程阻塞
     *
     * @throws Exception
     */
    private void await() throws Exception {

        if (null == prior_path) {
            throw new Exception("prior_path error");
        }

        final CountDownLatch latch = new CountDownLatch(1);
        //订阅比自己次小顺序节点的删除事件
        Watcher w = watchedEvent -> {
            log.info("监听到的变化 watchedEvent = " + watchedEvent);
            log.info("[WatchedEvent]节点删除");
            latch.countDown();
        };

        client.getData().usingWatcher(w).forPath(prior_path);
        latch.await(WAIT_TIME, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        //TODO
        return false;
    }

    private String getIdPath(String locked_path) {

        int index = locked_path.lastIndexOf(ZK_PATH + "/");
        if (index >= 0) {
            index += ZK_PATH.length() + 1;
            return index <= locked_path.length() ? locked_path.substring(index) : "";
        }
        return null;
    }

    /**
     * 校验锁
     *
     * @param waiters
     * @return
     */
    private boolean checkLocked(List<String> waiters) {

        //节点按照编号，升序排列
        Collections.sort(waiters);
        // 如果是第一个，代表自己已经获得了锁
        if (locked_id_path.equals(waiters.get(0))) {
            log.info("成功的获取分布式锁,节点为{}", locked_id_path);
            return true;
        }
        return false;
    }


    /**
     * 从zookeeper中拿到所有等待节点
     */
    protected List<String> getWaiters() {

        List<String> children = null;
        try {
            children = client.getChildren().forPath(ZK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }


}

