package com.aliencat.javabase.api.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABC {
    ReentrantLock lock = new ReentrantLock();
    Condition A = lock.newCondition();
    Condition B = lock.newCondition();
    Condition C = lock.newCondition();
    private int flag = 1;

    public void printA(int i) {
        lock.lock();
        try {
            while (flag != 1) {
                try {
                    A.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
            flag = 2;
            B.signal();
        } finally {
            lock.unlock();
        }
    }

    public void printB(int i) {
        lock.lock();
        try {
            while (flag != 2) {
                try {
                    B.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
            flag = 3;
            C.signal();
        } finally {
            lock.unlock();
        }
    }

    public void printC(int i) {
        lock.lock();
        try {
            while (flag != 3) {
                try {
                    C.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
            System.out.println("---------------------");
            flag = 1;
            A.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        PrintABC testABC = new PrintABC();
        Thread A = new Thread(new A(testABC), "A");
        Thread B = new Thread(new B(testABC), "B");
        Thread C = new Thread(new C(testABC), "C");
        A.start();
        B.start();
        C.start();
    }

    static class A implements Runnable {
        private PrintABC testABC;

        public A(PrintABC testABC) {
            this.testABC = testABC;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                testABC.printA(i + 1);
            }
        }
    }

    static class B implements Runnable {
        private PrintABC testABC;

        public B(PrintABC testABC) {
            this.testABC = testABC;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                testABC.printB(i + 1);
            }
        }
    }

    static class C implements Runnable {
        private PrintABC testABC;

        public C(PrintABC testABC) {
            this.testABC = testABC;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                testABC.printC(i + 1);
            }
        }
    }
}

