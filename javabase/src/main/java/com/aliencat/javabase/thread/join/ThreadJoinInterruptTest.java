package com.aliencat.javabase.thread.join;

import java.util.concurrent.TimeUnit;

/**
 * 使用join方法时，如何处理中断?
 * 在子线程中中断主线程，然后当主线程执行thread1.join()方法时，就会抛出异常，
 * 异常原因是主线程已经被中断了，所以此行代码不能继续执行了。
 * 此时，最优秀的做法是把主线程的中断状态传递给子线程，
 * 即在catch()语句中，执行thread1.interrupt()，
 * 这样此线程也会中断，维护了多线程之间的数据一致性。
 */
public class ThreadJoinInterruptTest {

    public static void main(String[] args) {
        // 获取主线程
        Thread mainThread = Thread.currentThread();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 在子线程中，让主线程中断
                    System.out.println("thread1已启动,在thread1的run方法中，让主线程中断停止了");
                    mainThread.interrupt();
                    TimeUnit.SECONDS.sleep(4);
                    System.out.println("thread1的run方法运行结束");
                } catch (InterruptedException e) {
                    System.out.println("子线程开始响应中断，抛出中断异常说明成功中断");
                }
            }
        }, "thread1");
        thread1.start();
        try {
            // 主线程在等待子线程执行结束后，再执行后续代码。如果主线程在等待时被打断，那thread1.join()会抛出异常，
            // 此时正确的做法是在catch语句中将中断传递给thread1,让thread1也中断，保证多个线程执行的一致性；
            // 若不手动终止thread1，则thread1会继续执行，可能会造成一些数据同步上的问题。
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println("主线程执行thread.join()方法时出现异常,提示主线程中断了（验证线程名：" + Thread.currentThread().getName() + ")");
            e.printStackTrace();
            thread1.interrupt();
        }
        System.out.println("main方法全部运行完毕");
    }

}
