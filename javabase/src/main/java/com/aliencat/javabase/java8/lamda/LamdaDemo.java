package com.aliencat.javabase.java8.lamda;

import org.junit.Test;

import java.util.function.Function;

public class LamdaDemo {

    @Test
    public void test1() {
        //Lambda表达式
        Function<Integer, String[]> fun = (len) -> new String[len];
        String[] arr1 = fun.apply(10);
        System.out.println(arr1 + ", " + arr1.length);

        //方法引用
        Function<Integer, String[]> fun2 = String[]::new;
        String[] arr2 = fun2.apply(5);
        System.out.println(arr2 + ", " + arr2.length);
    }

}
