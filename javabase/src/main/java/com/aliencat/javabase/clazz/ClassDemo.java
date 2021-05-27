package com.aliencat.javabase.clazz;

public class ClassDemo implements Cloneable{

    static int i;

    static {
        i++;
        System.out.println("静态代码块");
    }

    {
        i++;
        System.out.println("普通代码块");
    }

    ClassDemo(){
        i++;
        System.out.println("构造器");
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        ClassDemo classDemo = new ClassDemo();
        Class clazz = classDemo.getClass();
        ClassDemo classDemoClone = (ClassDemo) classDemo.clone();
        System.out.println(classDemo == classDemoClone);
        System.out.println(classDemo.i);
    }


}
