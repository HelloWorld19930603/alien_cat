package com.aliencat.javabase.clazz;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.openjdk.jol.info.ClassLayout;

public class ObjectDemo {

    static Object o = new Object();
    static Object[] objects = new Object[1];

    public static void main(String[] args) {

        //jdk提供的方法
        System.out.println("Object size:"+ObjectSizeCalculator.getObjectSize(o));
        System.out.println("Object[] size:"+ObjectSizeCalculator.getObjectSize(objects));
        //需要添加jol依赖
        //打印对象的布局信息
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        System.out.println(ClassLayout.parseInstance(objects).toPrintable());
    }
}
