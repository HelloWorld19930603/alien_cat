package com.pattern.proxy;

import com.pattern.proxy.entity.Person;
import com.pattern.proxy.factory.ProxyFactory;

/**
 * 代理模式
 * 为其他对象提供一种代理以控制对这个对象的访问。
 * <p>
 * <p>
 * 通过创建类的代理实现对类方法的增强，避免修改方法源代码。
 */
public class ProxyMain {
    public static void main(String[] args) throws Exception {
        Integer a = 1;

        for (int i = 0; i < 100; i++) {
            //
            double v = Math.random() * 9 + 1;

            double pow = Math.pow(10, 5);
            System.out.println((int) (v * 100000));
        }

        //通过代理工厂创建小明类
        Person xiaoming = ProxyFactory.builder(Person.class);
        xiaoming.eat();
    }
}
