package com.aliencat.javabase.thread.order;

public class ThreadTest {


    static int a = 1;

    static {
        System.out.println(a);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> a = 2);
        t1.join();
        System.out.println(a);
    }
}
