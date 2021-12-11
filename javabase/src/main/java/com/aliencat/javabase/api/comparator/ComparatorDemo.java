package com.aliencat.javabase.api.comparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorDemo {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("张三", 23),
                new Person("李四", 19),
                new Person("王五", 21)
        );
        Collections.sort(people, new LexicographicComparator());
        System.out.println(people);
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person a, Person b) {
                //根据年龄大小升序排列  正数表示a排在b前，负数表示b排在a前，0表示无变化
                return a.age < b.age ? -1 : a.age == b.age ? 0 : 1;
            }
        });
        System.out.println(people);
    }
}

class LexicographicComparator implements Comparator<Person> {
    @Override
    public int compare(Person a, Person b) {
        return a.name.compareToIgnoreCase(b.name);
    }
}

class Person {
    String name;
    int age;
    Person(String n, int a) {
        name = n;
        age = a;
    }
    @Override
    public String toString() {
        return String.format("{name=%s, age=%d}", name, age);
    }
}
