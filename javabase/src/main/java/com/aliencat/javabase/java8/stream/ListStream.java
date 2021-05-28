package com.aliencat.javabase.java8.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ListStream {

    public List newArrayList(Integer... arr){
        List<Integer> list = new ArrayList<>();
        for(Integer i : arr){
            list.add(i);
        }
        return list;
    }


    @Test
    public void test1(){
        List<Integer> nums = newArrayList(1,null,3,4,null,6);
        long l = nums.stream().filter(num -> num != null).count();
        System.out.println(l);
    }

    //of方法：有两个overload方法，一个接受变长参数，一个接口单一值
    @Test
    public void test2(){
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 5);
        Stream<String> stringStream = Stream.of("taobao");

    }

    //generator方法：生成一个无限长度的Stream，
    // 其元素的生成是通过给定的Supplier（这个接口可以看成一个对象的工厂，每次调用返回一个给定类型的对象）
    @Test
    public void test3() {
        Stream.generate(new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        });
        //下面两条语句的作用都是一样的，只是使用了lambda表达式和方法引用的语法来简化代码。
        Stream.generate(() -> Math.random());
        Stream.generate(Math::random);
    }

    //iterate方法：也是生成无限长度的Stream，和generator不同的是，
    // 其元素的生成是重复对给定的种子值(seed)调用用户指定函数来生成的。
    // 其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环
    @Test
    public void test4() {
        Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);
    }
}
