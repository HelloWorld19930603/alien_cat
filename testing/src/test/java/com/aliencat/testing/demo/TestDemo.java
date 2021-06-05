package com.aliencat.testing.demo;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

//a==1 && a==2 && a==3 是 true
public class TestDemo {

    @Test
    public void test1() throws NoSuchFieldException, IllegalAccessException {
        Class cache = Integer.class.getDeclaredClasses()[0];
        Field c = cache.getDeclaredField("cache");
        c.setAccessible(true);
        Integer[] array = (Integer[]) c.get(cache);
        // array[129] is 1
        array[130] = array[129];
         // Set 2 to be 1
        array[131] = array[129];
        // Set 3 to be 1
        Integer a = 1;
        if (a == (Integer) 1 && a == (Integer) 2 && a == (Integer) 3) {
            System.out.println("Success");
        }
        System.out.println(++a);
        a = 100;
        Integer b = 100;
        System.out.println(a == b);
    }

    Object o = new Object();
    @Test
    public void test2(){
        Object o2 = o;
        test3(o2);
        System.out.println(o2 == null);
    }

    public void test3(Object o){
        o = null;
        System.out.println(this.o == null);
    }

    public void test3(){
        HashMap map = new HashMap();
    }

}
