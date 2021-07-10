package com.aliencat.javabase.experiment;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CycleTimeTest {


    @Test
    public void test() {
        int[] arr = new int[100000];
        int sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i] + 2 * i;
        }
        log.info("循环1耗时：" + (System.nanoTime() - start));
        sum = 0;
        start = System.nanoTime();
        for (int i = 0; i < arr.length; i += 10) {
            sum += arr[i] + 2 * i;
        }
        log.info("循环2耗时：" + (System.nanoTime() - start));
    }
}
