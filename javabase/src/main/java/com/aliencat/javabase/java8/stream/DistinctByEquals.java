package com.aliencat.javabase.java8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Author chengcheng
 * @Date 2022-08-08
 **/

public class DistinctByEquals {
    public static void main(String[] args) {
        Person person1 = new Person();
        person1.setName("张三");
        Person person2 = new Person();
        person2.setName("张三");
        Person person3 = new Person();
        person3.setName("李四");
        List<Person> persons = new ArrayList();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        System.out.println("去重前:");
        persons.stream().forEach(x->System.out.println(x.toString()));
        System.out.println("去重后");
        persons.stream().filter(distinctByField(person -> person.getName())).forEach(x->System.out.println(x.toString()));
    }

    static <T> Predicate<T> distinctByField(Function<? super T,?> fieldExtractor){
        Map<Object,Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(fieldExtractor.apply(t), Boolean.TRUE) == null;
    }

}

