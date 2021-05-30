package com.aliencat.javabase.api.thread;

public class ProductionAndConsumption {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Thread thread1 = new Thread(new Producer(resource), "生产者1");
        Thread thread2 = new Thread(new Producer(resource), "生产者2");
        Thread thread3 = new Thread(new Consumer(resource), "消费者1");
        Thread thread4 = new Thread(new Consumer(resource), "消费者2");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

    }
}

/**
 * 表示产品资源
 */
class Resource {
    //标号
    private int count;
    //名字
    private String name;

    //标志位,false表示没有产品,trur表示生产出了产品
    private boolean flag;

    /**
     * 生产产品
     * @param name
     */
    void set(String name) {
        //使用同步块
        synchronized (this) {
            //判断false是否为true,如果是true说明有产品了,那么生产者线程应该等待
            if (flag) {
                try {
                    System.out.println("有产品了--" + Thread.currentThread().getName() + "生产等待");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //走到这一步,说明没有产品,可以生产
            this.name = name;
            System.out.println(Thread.currentThread().getName() + "--" + this.name + (++count) + "生产");
            //设置产品标志为true,表示有产品了,可以消费了
            flag = true;
            //这里唤醒所有线程,有可能还会唤醒生产者
            this.notifyAll();
        }
    }

    /**
     * 消费产品
     */
    void get() {
        synchronized (this) {
            //判断flag是否为false,如果是fasle说明有产品了,那么消费者线程应该等待
            if (!flag) {
                try {
                    System.out.println("没产品了--" + Thread.currentThread().getName() + "消费等待");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //走到这一步,说明有产品,可以消费
            System.out.println(Thread.currentThread().getName() + "--" + this.name + count + "消费");
            //设置产品标志为false,表示没有产品了,可以生产了
            flag = false;
            //这里唤醒所有线程,有可能还会唤醒消费者
            this.notifyAll();
        }
    }
}

/**
 * 生产者线程
 */
class Producer implements Runnable {
    private Resource resource;

    public Producer(Resource resource) {
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
            //调用生产方法
            resource.set("面包");
        }
    }
}

/**
 * 消费者线程
 */
class Consumer implements Runnable {
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


