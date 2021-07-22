package com.aliencat.javabase.experiment;

import lombok.extern.slf4j.Slf4j;

/**
 * 主线程死亡后，子线程还能运行吗？
 */
@Slf4j
public class SubthreadSurvivalTest {


    //非守护线程不会随着主线程的结束而结束
    public static void main(String[] args) throws InterruptedException {
        final Thread mainThread = Thread.currentThread();
        log.info("开启主线程");
        Thread subThread = new Thread(() -> {
            log.info("进入子线程");
            try {
                mainThread.join();
                log.info("子线程进入休眠");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("结束子线程"); //若是守护线程则在主线程结束后不会执行到此行代码，且不会抛出异常
        });
        //subThread.setDaemon(true);  //设置为守护线程
        subThread.start();
        Thread.sleep(1000);
        log.info("结束主线程");
    }

}
