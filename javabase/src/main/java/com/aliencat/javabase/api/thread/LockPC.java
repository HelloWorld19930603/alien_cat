package com.aliencat.javabase.api.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockPC {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Producer producer = new Producer(resource);
        Consumer consumer = new Consumer(resource);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.execute(producer);
        threadPoolExecutor.execute(consumer);
        threadPoolExecutor.execute(producer);
        threadPoolExecutor.execute(consumer);
        threadPoolExecutor.shutdown();
    }

    /**
     * 产品资源
     */
    static class Resource {
        private String name;
        private int count;
        boolean flag;
        //获取lock锁,lock锁的获取和释放需要代码手动操作
        ReentrantLock lock = new ReentrantLock();
        //从lock锁获取一个condition,用于生产者线程在此等待和唤醒
        Condition producer = lock.newCondition();
        //从lock锁获取一个condition,用于消费者线程在此等待和唤醒
        Condition consumer = lock.newCondition();

        void set(String name) {
            //获得锁
            lock.lock();
            try {
                while (flag) {
                    try {
                        System.out.println("有产品了--" + Thread.currentThread().getName() + "生产等待");
                        //该生产者线程,在producer上等待
                        producer.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ++count;
                this.name = name;
                System.out.println(Thread.currentThread().getName() + "生产了" + this.name + +count);
                flag = !flag;
                //唤醒在consumer上等待的消费者线程,这样不会唤醒等待的生产者
                consumer.signalAll();
            } finally {
                //释放锁
                lock.unlock();
            }
        }

        void get() {
            lock.lock();
            try {
                while (!flag) {
                    try {
                        System.out.println("没产品了--" + Thread.currentThread().getName() + "消费等待");
                        //该消费者线程,在consumer上等待
                        consumer.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "消费了" + this.name + count);
                flag = !flag;
                //唤醒在producer监视器上等待的生产者线程,这样不会唤醒等待的消费者
                producer.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 消费者
     */
    static class Consumer implements Runnable {
        private Resource resource;

        public Consumer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //调用消费方法
                resource.get();
            }
        }
    }

    /**
     * 生产者
     */
    static class Producer implements Runnable {
        private Resource resource;

        public Producer(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);  //生产耗时1s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //调用生产方法
                resource.set("面包");
            }
        }
    }
}

