package com.aliencat.javabase.java8.lamda;

import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaDemo {

    @Test
    public void test1() {
        //Lambda表达式
        Function<Integer, String[]> fun = (len) -> new String[len];
        String[] arr1 = fun.apply(9);
        System.out.println(arr1 + ", " + arr1.length);

        //方法引用
        Function<Integer, String[]> fun2 = String[]::new;
        String[] arr2 = fun2.apply(6);
        System.out.println(arr2 + ", " + arr2.length);
    }

    //类名::new 引用构造器
    @Test
    public void test2() {
        //Lambda表达式
        Supplier<Person> sup = () -> new Person();
        System.out.println(sup.get());
        //方法引用
        Supplier<Person> sup2 = Person::new;
        System.out.println(sup2.get());
        //带入参的方法引用，BiFunction(String, Integer为方法入参，Person为出参)
        BiFunction<String, Integer, Person> fun2 = Person::new;
        System.out.println(fun2.apply("张三", 30));
    }

    //类名::引用实例方法
    @Test
    public void test3() {
        //Lambda表达式
        Function<String, Integer> f1 = (s) -> s.length();
        System.out.println(f1.apply("ABC123"));
        //方法引用
        Function<String, Integer> f2 = String::length;
        System.out.println(f2.apply("ABC123"));
    }

    //Runnable 接口的匿名内部类
    @Test
    public void test4() {
        //原匿名内部类写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("新线程任务执行！");
            }
        }).start();

        //使用lambda表达式写法
        new Thread(() -> System.out.println("新线程任务执行！")).start(); // 启动线程
    }
}


class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
    }
}