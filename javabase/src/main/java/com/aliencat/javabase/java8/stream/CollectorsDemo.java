package com.aliencat.javabase.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectorsDemo {

    public static void main(String[] args) {

        List<Person> list = new ArrayList();
        list.add(new Person(1, "zhangsan"));
        list.add(new Person(2, "lisi"));
        list.add(new Person(3, "wangwu"));
        list.add(new Person(3, "zhaoliu"));

        /**
         * Collectors.toMap 作用是将List 转成map
         * Collectors.toMap(key,v->v,(v1,v2)->v1)
         * 其中key 就是map得key值
         * 第二个参数就是map得value
         * 第三个参数的作用是当出现一样的key值得时候如何取舍其中V1代表旧值，v2代表新值，示例中取旧值
         *
         * Function.identity()返回一个输出跟输入一样的Lambda表达式对象
         */
        Map<Integer, Person> map1 = list.stream()
                .collect(Collectors.toMap(Person::getId, Function.identity(), (p1, p2) -> p1));
        System.out.println(map1);
        System.out.println(map1.get(1).getName());
        Map<Person, String> map2 = list.stream()
                .collect(Collectors.toMap(Function.identity(), Person::getName));
        System.out.println(map2);

    }

}


