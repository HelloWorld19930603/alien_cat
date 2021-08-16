package com.aliencat.javabase.java8.stream;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 题目:请按照给出数据,找出同时满足以下条件的用户,也即以下条件全部满足
 * 偶数ID且年龄大于20且用户名转为大写且用户名字母倒排序
 * 只输出一个用户名字
 */
public class StreamDemo {

    public static void main(String[] args) {
        User u1 = new User(11, 33, "zhangsan");
        User u2 = new User(12, 24, "lisi");
        User u3 = new User(13, 12, "wangwu");
        User u4 = new User(14, 28, "zhaoliu");
        User u5 = new User(16, 26, "sunqi");
        List<User> list = Arrays.asList(u1, u2, u3, u4, u5);
        list.stream()
                .filter(s -> s.getId() % 2 == 0)
                .filter(s -> s.getAge() > 20)
                .map(s -> s.getUserName().toUpperCase())
                .sorted((s1, s2) -> s2.compareTo(s1))
                .limit(1)
                .forEach(System.out::print);
    }
}

@Data
@AllArgsConstructor
class User {

    int id;
    int age;
    String userName;
}