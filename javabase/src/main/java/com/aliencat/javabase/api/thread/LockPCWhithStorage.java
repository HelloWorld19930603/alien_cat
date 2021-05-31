package com.aliencat.javabase.api.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockPCWhithStorage {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Consumer c = new Consumer(resource);
        Producer p = new Producer(resource);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.execute(p);
        threadPoolExecutor.execute(p);
        threadPoolExecutor.execute(c);
        threadPoolExecutor.execute(c);
        threadPoolExecutor.shutdown();
    }

    static class Resource {
        // 获得锁对象
        final Lock lock = new ReentrantLock();
        // 获得生产监视器
        final Condition notFull = lock.newCondition();
        // 获得消费监视器
        final Condition notEmpty = lock.newCondition();

        // 定义一个数组，当作仓库，用来存放商品
        final Object[] items = new Object[100];
        /*
         * 取消了falg标志,取而代之的是用仓库的数量来判断是否应该阻塞或者唤醒对应的线程
         * putpur：生产者使用的下标索引；
         * takeptr：消费者下标索引；
         * count：用计数器，记录商品个数
         */
        int putptr, takeptr, count;

        // 生产方法
        public void put(Object x) {
            // 获得锁
            lock.lock();
            try {
                // 如果商品个数等于数组的长度，商品满了将生产将等待消费者消费
                while (count == items.length) {
                    try {
                        System.out.println("仓库满了--" + Thread.currentThread().getName() + "生产等待");
                        notFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 生产索引对应的商品，放在仓库中
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                items[putptr] = x;
                // 如果下标索引加一等于数组长度，将索引重置为0，重新开始
                if (++putptr == items.length) {
                    putptr = 0;
                }
                // 商品数加1
                ++count;
                System.out.println(Thread.currentThread().getName() + "生产了" + x + "共有" + count + "个");
                // 唤醒消费线程
                notEmpty.signal();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }

        // 消费方法
        public Object take() {
            //获得锁
            lock.lock();
            try {
                //如果商品个数为0.消费等待
                while (count == 0) {
                    try {
                        System.out.println("仓库空了--" + Thread.currentThread().getName() + "消费等待");
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //获得对应索引的商品，表示消费了
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object x = items[takeptr];
                //如果索引加一等于数组长度，表示取走了最后一个商品，消费完毕
                if (++takeptr == items.length)
                //消费索引归零，重新开始消费
                {
                    takeptr = 0;
                }
                //商品数减一
                --count;
                System.out.println(Thread.currentThread().getName() + "消费了" + x + "还剩" + count + "个");
                //唤醒生产线程
                notFull.signal();
                //返回消费的商品
                return x;
            } finally {
                //释放锁
                lock.unlock();
            }
        }
    }


    static class Producer implements Runnable {

        private Resource resource;

        public Producer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            while (true) {
                resource.put("面包");
            }
        }
    }

    static class Consumer implements Runnable {
        private Resource resource;

        public Consumer(Resource resource) {
            this.resource = resource;
        }


        @Override
        public void run() {
            while (true) {
                resource.take();
            }
        }
    }
}

