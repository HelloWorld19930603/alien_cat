package com.aliencat.javabase.experiment;

/**
 * 多重循环下终止和跳过最外层循环的新语法测试
 * 偶然在JDK源码中发现的终止循环的新语法
 * 故在此测试以下
 */
public class BreakLoopTest {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis() / 1000;
        int i = 1, j = 1;
        retry:
        for (; ; ) {
            System.out.println("外循环开始 : " + i++);
            Thread.sleep(1000);
            for (; ; ) {
                System.out.println("内循环开始: " + j++);
                long nowTime = System.currentTimeMillis() / 1000;
                if (nowTime - startTime > 10) {
                    break retry;  //10s后终止外循环
                }
                if (nowTime % 2 == 0) {
                    continue retry;  // 50%概率跳过当前循环
                }
                Thread.sleep(1000);
                System.out.println("内循环结束");
            }
            //System.out.println("外循环结束");  //此处执行不到
        }
    }


}
