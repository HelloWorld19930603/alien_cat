package com.aliencat.javabase.thread.stop;

/**
 * 线程终止的三种方式测试
 * 线程中止的三种方式：stop、interrupt 以及标志位
 */
public class StopThreadTest implements Runnable {

    static int i = 0;
    static int j = 0;
    static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {

        /**
         * 第一种通过stop方法
         * 该方法中止线程，并且清除监控器锁的信息，但是可能导致线程安全问题，JDK 不建议使用
         * 这里如果用 stop 方法将线程中止的话，会导致 i 和 j 数据不正确
         */
        Thread stopThread = new Thread(new StopThreadTest());
        stopThread.start();
        Thread.sleep(1000);
        stopThread.stop();
        stopThread.join();
        print();

        /**
         *第二种通过interrupt方法
         *interrupt 不会强制中止，将线程直接中断，而是抛出异常通知我们，
         *开发者就可以控制收到异常后的执行逻辑，让整个程序处于线程安全的状态
         *这是目前 JDK 版本中推荐的 interrupt 方法。
         */
        Thread interuptThread = new Thread(new StopThreadTest());
        interuptThread.start();
        Thread.sleep(1000);
        interuptThread.interrupt();
        interuptThread.join();
        print();

        /**
         * 第三种，通过设置共享标记
         * 如果代码程序逻辑中是循环执行的业务，可以在程序的执行中线程代码中增加一个标志位，
         * 比如在 while 循环中去执行这个程序，通过 flag 去控制程序是否继续执行，
         * 如果在外部线程将 flag 修改为 false，那么创建的子线程代码中会收到这个数据的变化，
         * 通过这个变量的形式，通知到另一个线程，从而达到控制线程中止的效果。
         */
        Thread flagThread = new Thread(new StopThreadTest());
        flagThread.start();
        Thread.sleep(1000);
        flag = false;
        flagThread.join();
        print();
    }

    /**
     * 状态重置
     */
    public static void reset() {
        i = 0;
        j = 0;
    }

    public static void print() {
        System.out.println("i=" + i + " j=" + j);
        reset();
    }

    /**
     * 这个线程做的事情就是在同步代码块中对 i 和 j 这两个变量进行自增操作，
     * 但是在这个执行过程中会进行 2 秒的睡眠
     */
    public void run() {
        i++;
        try {
            while (flag)
                Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("线程中断了");
        }
        j++;
    }
}
