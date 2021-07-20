package com.aliencat.javabase.experiment;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 代码运行一万次和十万次是一样快的？
 */
@Slf4j
public class CycleTimeTest {

    //当arr长度较小时，比如1M大小，我在i7环境下测试两个循环耗时相差5倍以上
    @Test
    public void test1() {
        int[] arr = new int[1024 * 1024];
        int len = arr.length;
        long start = System.nanoTime();
        for (int i = 0; i < len; i++) {  //循环len次
            arr[i] *= 2;
        }
        log.info("循环1耗时：" + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < len; i += 16) {  //循环len/16次
            arr[i] *= 2;
        }
        log.info("循环2耗时：" + (System.nanoTime() - start));
    }

    //当arr长度较小时，比如128M大小，我在i7环境下测试两个循环耗时相差无几
    //也就是说随着arr数组大小的增大，两个循环的效率是不断趋近的
    @Test
    public void test2() {
        int[] arr = new int[128 * 1024 * 1024];
        int len = arr.length;
        long start = System.nanoTime();
        for (int i = 0; i < len; i++) {  //循环len次
            arr[i] *= 2;
        }
        log.info("循环1耗时：" + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < len; i += 16) {   //循环len/16次
            arr[i] *= 2;
        }
        log.info("循环2耗时：" + (System.nanoTime() - start));
    }

    //cpu在进行计算的时候，是先将数据写到高速缓存中，每次计算cpu就读取一个缓存行，而一个缓存行的大小是64字节
    @Test
    public void test3() {
        int[] arr = new int[640 * 1024 * 1024];
        int len = arr.length;
        long start = System.nanoTime();
        for (int i = 0; i < len; i++) {  //循环len次
            arr[i] *= 2;
        }
        log.info("循环1耗时：" + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < len; i += 16) {   //循环len/16次
            arr[i] *= 2;
        }
        log.info("循环2耗时：" + (System.nanoTime() - start));
    }

    //cpu在进行计算的时候，是先将数据写到高速缓存中，每次计算cpu就读取一个缓存行，而一个缓存行的大小是64字节
    //可能由于cpu超线程的缘故，当我将循环比置为1 ：32后，并没有得到期望的耗时比 2:1的结果，而是将近3:1的样子
    @Test
    public void test4() {
        int[] arr = new int[640 * 1024 * 1024];
        int len = arr.length;
        long start = System.nanoTime();
        for (int i = 0; i < len; i++) {  //循环len次
            arr[i] *= 2;
        }
        log.info("循环1耗时：" + (System.nanoTime() - start));
        start = System.nanoTime();
        for (int i = 0; i < len; i += 32) {   //循环len/32次
            arr[i] *= 2;
        }
        log.info("循环2耗时：" + (System.nanoTime() - start));
    }
}
