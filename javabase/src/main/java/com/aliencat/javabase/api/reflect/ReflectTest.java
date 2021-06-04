package com.aliencat.javabase.api.reflect;

import org.junit.Test;

public class ReflectTest {


    @Test
    public void test1() throws ClassNotFoundException {
        Class c1 = new Object().getClass();
        Class c2 = Object.class;
        Class c3 = Class.forName("java.lang.Object");

        System.out.println(c1==c2 && c2==c3);
    }
}
