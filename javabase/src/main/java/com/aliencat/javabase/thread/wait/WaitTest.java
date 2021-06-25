package com.aliencat.javabase.thread.wait;

public class WaitTest {

    public static WaitTest waitTest = new WaitTest();
    public int i = 0;

    public static void main(String[] args) {

        new WaitThread().start();
        new WaitThread().start();
        new WaitThread().start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new NotifyAllThread().start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (WaitTest.class) {
            WaitTest.class.notify();
        }

    }


}

class WaitThread extends Thread {

    @Override
    public void run() {
        synchronized (WaitTest.class) {
            try {
                WaitTest.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(WaitTest.waitTest.i++);
        }
    }
}

class NotifyAllThread extends Thread {

    @Override
    public void run() {
        synchronized (WaitTest.class) {
            //WaitTest.class.notifyAll(); //输出0 1 2
            WaitTest.class.notify();
            try {
                WaitTest.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}