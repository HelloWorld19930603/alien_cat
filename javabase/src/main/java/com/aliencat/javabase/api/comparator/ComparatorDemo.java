package com.aliencat.javabase.api.comparator;

import java.util.*;

public class ComparatorDemo {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("张三", 23),
                new Person("李四", 19),
                new Person("王五", 21)
        );
        System.out.println(people);
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person a, Person b) {
                //根据年龄大小升序排列  正数表示a排在b前，负数表示b排在a前，0表示无变化
                return a.age < b.age ? -1 : a.age == b.age ? 0 : 1;
            }
        });
        System.out.println(people);
        System.out.println("--------------------------------");


        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("小米", 12,80));
        students.add(new Student("小红", 13,70));
        students.add(new Student("小明", 11,66));
        students.add(new Student("小方", 15,91));
        System.out.println(students);
        //这里会自动调用Person中重写的compareTo方法。
        Collections.sort(students);
        System.out.println(students);
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

class Student extends Person implements Comparable<Student>{

    int score;

    Student(String n, int a,int s) {
        super(n,a);
        score = s;
    }
    @Override
    public int compareTo(Student person) {
        return name.compareTo(person.name);
        //相当于 return this.name - person.name;
    }

    @Override
    public String toString() {
        return String.format("{name=%s, age=%d, score=%d}", name, age, score);
    }
}