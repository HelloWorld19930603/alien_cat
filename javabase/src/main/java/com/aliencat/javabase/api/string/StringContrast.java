package com.aliencat.javabase.api.string;


import java.util.concurrent.locks.ReentrantLock;

/**
 * - String：String对象是不可变的。对String对象的任何改变都不影响到原对象的内容，
 * 相关的任何change操作都会导致该字符串变量指向新的对象的地址。
 * - StringBuilder：StringBuilder是可变的（存入的值的内容和长度都是可改变的，且引用地址不变），它不是线程安全的。
 * - StringBuffer：StringBuffer也是可变的，它是线程安全的，所以它的开销比StringBuilder大
 */
public final class StringContrast {

    static ReentrantLock lock = new ReentrantLock();

    public static void performanceTest(int frequency) {
        String str = "";
        StringBuilder stringBuilder = new StringBuilder();
        StringBuffer stringBuffer = new StringBuffer();

        long totalTime = 0;//记录每个对象记录测试总耗时
        long time;         //记录每个对象记录一个周期测试耗时
        int i = 0;
        int cycle = 10;    //测试周期
        for (int j = 0; j < cycle; j++) {
            //时间单位为纳秒
            time = System.nanoTime();
            while (i++ < frequency) {
                str += "A";
            }
            totalTime += System.nanoTime() - time;
            str = "";
        }
        System.out.println("str          累加" + frequency + "个长度为1的字符串,平均耗时为："
                + totalTime / cycle);

        totalTime = i = 0;
        for (int j = 0; j < cycle; j++) {
            time = System.nanoTime();
            while (i++ < frequency) {
                stringBuffer.append("A");
            }
            totalTime += System.nanoTime() - time;
            stringBuffer = new StringBuffer();
        }
        System.out.println("stringBuffer 累加" + frequency + "个长度为1的字符串,平均耗时为："
                + totalTime / cycle);

        totalTime = i = 0;
        for (int j = 0; j < cycle; j++) {
            time = System.nanoTime();
            while (i++ < frequency) {
                stringBuilder.append("A");
            }
            totalTime += System.nanoTime() - time;
            stringBuilder = new StringBuilder();
        }
        System.out.println("stringBuilder累加" + frequency + "个长度为1的字符串,平均耗时为："
                + totalTime / cycle);

        stringBuffer = new StringBuffer();
        //由于JIT对单线程下锁优化，所以即使这里加锁，也几乎不会影响效率
        totalTime = i = 0;
        for (int j = 0; j < cycle; j++) {
            lock.lock();
            try {
                time = System.nanoTime();
                while (i++ < frequency) {
                    stringBuffer.append("A");
                }
                totalTime += System.nanoTime() - time;
                stringBuffer = new StringBuffer();
            } finally {
                lock.unlock();
            }
        }
        System.out.println("stringBuffer 累加" + frequency + "个长度为1的字符串,平均耗时为："
                + totalTime / cycle);

        System.out.println();
    }

    public static void main(String[] args) {
        performanceTest(100);
        performanceTest(10000);
        performanceTest(100000);
    }

}
