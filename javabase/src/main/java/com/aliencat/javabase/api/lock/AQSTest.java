package com.aliencat.javabase.api.lock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AQSTest implements Runnable {

    static int count = 0;
    static MyAQSLock lock = new MyAQSLock();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(new AQSTest()).start();
        }

    }

    @Override
    public void run() {
        try {
            lock.lock();
            for (int i = 0; i < 10000; i++) {
                lock.lock();
                count++;
                lock.unLock();
            }
            log.info(Thread.currentThread().getName() + " count:" + count);
        } finally {
            lock.unLockAll();  //解除所有锁，这样里面的unlock就不用加try语句包裹了
        }

    }
}
