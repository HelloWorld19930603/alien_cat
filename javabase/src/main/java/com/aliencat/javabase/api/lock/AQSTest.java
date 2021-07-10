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
        lock.lock();
        for (int i = 0; i < 10000; i++)
            count++;
        log.info("count:" + count);
        lock.unLock();
    }
}
