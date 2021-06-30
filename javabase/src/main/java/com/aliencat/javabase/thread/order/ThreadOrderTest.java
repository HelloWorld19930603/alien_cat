package com.aliencat.javabase.thread.order;

import java.util.concurrent.CountDownLatch;

/**
 * 关于多线程下代码执行顺序的测试
 */
public class ThreadOrderTest {

    private static int x, y, a, b;

    public static void main(String[] args) {
        int i = 1;
        while (true) {
            //CountDownLatch用来保证两个线程的赋值操作，尽可能同时
            CountDownLatch countDownLatch = new CountDownLatch(1);
            a = 0;
            b = 0;
            x = 0;
            y = 0;
            Thread t1 = new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a = 1;
                x = b;
            });
            Thread t2 = new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                b = 1;
                y = a;
            });

            t1.start();
            t2.start();

            countDownLatch.countDown();

            try {
                t1.join();
                t2.join();   //让当前线程在子线程结束后再执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String result = "第" + (i++) + "次（" + x + "," + y + ")";
            if (x == 0 && y == 0) { //x和y同时为0的情况，就是发生重排序
                System.out.println(result);
                break;
            }/* else {
                System.out.println(result);
            }*/
        }
    }
}
