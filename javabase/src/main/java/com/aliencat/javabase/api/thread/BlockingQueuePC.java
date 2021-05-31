package com.aliencat.javabase.api.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BlockingQueuePC {
    //定义一个阻塞队列
    static LinkedBlockingQueue<Object> objects = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        Resource resource = new Resource("面包");
        Consumer consumer = new Consumer();
        Producer producer = new Producer(resource);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 0,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        //启动多个生产者\消费者线程
        threadPoolExecutor.execute(producer);
        threadPoolExecutor.execute(consumer);
        threadPoolExecutor.execute(producer);
        threadPoolExecutor.execute(consumer);
        threadPoolExecutor.execute(consumer);
        threadPoolExecutor.shutdown();
    }

    /**
     * 消费者
     */
    static class Consumer implements Runnable {

        public Object take() throws InterruptedException {
            return objects.take();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Object take = take();
                    System.out.println(Thread.currentThread().getName() + "消费了" + take + ",还剩" + objects.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生产者
     */
    static class Producer implements Runnable {
        Resource resource;

        public Producer(Resource resource) {
            this.resource = resource;
        }

        public void put(Object o) throws InterruptedException {
            objects.put(o);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    put(resource);
                    System.out.println(Thread.currentThread().getName() + "生产了" + resource + ",还剩" + objects.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 产品/资源
     */
    static class Resource {
        String name;

        public Resource(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
