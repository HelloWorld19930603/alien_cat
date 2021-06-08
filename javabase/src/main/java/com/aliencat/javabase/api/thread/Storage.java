package com.aliencat.javabase.api.thread;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Storage {

    // 仓库存储的载体
    private LinkedList<Object> list = new LinkedList<Object>();
    // 仓库的最大容量
    final Semaphore notFull = new Semaphore(10);
    // 将线程挂起，等待其他来触发
    final Semaphore notEmpty = new Semaphore(0);
    // 互斥锁
    final Semaphore mutex = new Semaphore(1);

    public void produce() {
        try {
            notFull.acquire();
            mutex.acquire();
            list.add(new Object());
            System.out.println("【生产者" + Thread.currentThread().getName()
                    + "】生产一个产品，现库存" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mutex.release();
            notEmpty.release();
        }
    }

    public void consume() {
        try {
            notEmpty.acquire();
            mutex.acquire();
            list.remove();
            System.out.println("【消费者" + Thread.currentThread().getName()
                    + "】消费一个产品，现库存" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mutex.release();
            notFull.release();
        }
    }
}

