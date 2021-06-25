package com.aliencat.javabase.thread;

public class ABCTest {
    /**
     * 有三个线程 A,B,C
     * A为什么总是在C前面抢到锁？？？
     */

    private final static Object LOCK = new Object();


    public void startThreadA() {
        new Thread(() -> {
            synchronized (LOCK) {
                System.out.println(Thread.currentThread().getName() + ": get lock");
                //启动线程b
                startThreadB();
                System.out.println(Thread.currentThread().getName() + ": start wait");
                try {
                    //线程a wait
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": get lock after wait");
                System.out.println(Thread.currentThread().getName() + ": release lock");
            }
        }, "thread-A").start();
    }


    private void startThreadB() {
        new Thread(() -> {
            synchronized (LOCK) {
                System.out.println(Thread.currentThread().getName() + ": get lock");
                //启动线程c
                startThreadC();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": start notify");
                //线程b唤醒其他线程
                LOCK.notify();
                System.out.println(Thread.currentThread().getName() + ": release lock");
            }
        }, "thread-B").start();
    }


    private void startThreadC() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": thread c start");
            synchronized (LOCK) {
                System.out.println(Thread.currentThread().getName() + ": get lock");
                System.out.println(Thread.currentThread().getName() + ": release lock");
            }
        }, "thread-C").start();
    }

    /**
     * 通过源码我们看到，为何总是唤醒线程A，这是用于当线程C竞争不到锁时，被放入了cxq队列，而此时entrylist为null，
     * 线程A在等待集waitset中，当我们调用notify方法时，由于移动策略默认是2，这时会从等待集的头部将线程A取下，
     * 放入到entrylist中，当notify执行完毕后，在执行后面的monitor_exit字节码时将会优先从entrylist中唤醒线程，
     * 这就导致了A线程总是被优先执行。
     */
    public static void main(String[] args) {
        new ABCTest().startThreadA();
    }
}
/**
 *
 **/