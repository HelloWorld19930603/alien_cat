package com.aliencat.javabase.experiment;

import org.junit.Test;

public class TestDemo {


    @Test
    public void test1() {

        int i = 1000000007;

        for (int j = 0; j < 32; j++) {
            System.out.print((i & 1) == 1 ? 1 : 0);
            i >>= 1;
        }
    }
}
