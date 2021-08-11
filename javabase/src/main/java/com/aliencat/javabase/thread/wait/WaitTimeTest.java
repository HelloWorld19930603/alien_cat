package com.aliencat.javabase.thread.wait;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class WaitTimeTest {

    static Object lock = new Object();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            synchronized (lock) {
                log.info("A进入同步块，准备调用wait..");
                try {
                    log.info("wait前的线程状态：" + Thread.currentThread().getState());
                    lock.wait(1);
                    log.info("wait后的线程状态：" + Thread.currentThread().getState());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.notify();
                log.info("A被唤醒：" + Thread.currentThread().getState());
            }
        }, "A");
        threadA.start();

        Thread threadB = new Thread(() -> {
            synchronized (lock) {
                log.info("B进入同步块，准备调用sleep..");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("线程A的状态：" + threadA.getState());
                try {
                    lock.wait();  //释放锁，否则A无法继续执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("B被唤醒：" + Thread.currentThread().getState());
            }
        }, "B");
        threadB.start();
    }
}
