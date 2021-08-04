package com.aliencat.javabase.api.string;

/**
 * 使用intern()测试执行效率：空间使用上和使用耗时
 * <p>
 * 通常使用new创建的字符串都会在字符串常量池中进行备份(通过传入字符数组参数创建的除外)
 * 通过new创建的字符串使用其intern()方法返回常量池中的引用可以触发垃圾回收器对同一个堆中字符串的回收
 * 结论：对于堆中大量存在存在的字符串，尤其其中存在很多重复字符串时，使用intern()可以节省内存空间；
 * 同时，intern 也可以加快访问速度
 */
public class StringInternEfficiencyTest {
    static final int MAX_COUNT = 1000 * 10000;
    static final String[] arr = new String[MAX_COUNT];

    public static void main(String[] args) {
        Integer[] data = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        long start = System.currentTimeMillis();
        for (int i = 0; i < MAX_COUNT; i++) {
            //arr[i] = new String(String.valueOf(data[i % data.length]));   //花费的时间为：2952
            //显然 intern 加快了字符串操作任务的执行速度
            arr[i] = new String(String.valueOf(data[i % data.length])).intern(); // 花费的时间为：1119
        }
        long end = System.currentTimeMillis();
        System.out.println("花费的时间为：" + (end - start));

        System.gc();
        try {
            Thread.sleep(3000);  //停3s让gc先跑一会，虽然并不确定gc到底动没动
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //查看内存占用情况
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory();
        long total = run.totalMemory();
        long free = run.freeMemory();
        long usable = max - total + free;

        System.out.println("最大内存 = " + max);
        System.out.println("已分配内存 = " + total);
        System.out.println("已分配内存中的剩余空间 = " + free);
        System.out.println("最大可用内存 = " + usable);

        /**
         * 不使用intern():
         * 花费的时间为：2951
         * 最大内存 = 3797417984
         * 已分配内存 = 728236032
         * 已分配内存中的剩余空间 = 203256152
         * 最大可用内存 = 3272438104
         *
         * 使用intern()后:
         * 花费的时间为：1300
         * 最大内存 = 3797417984
         * 已分配内存 = 428867584
         * 已分配内存中的剩余空间 = 381250432
         * 最大可用内存 = 3749800832
         */
    }
}

