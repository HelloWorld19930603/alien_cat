package com.pattern.proxy.factory;

import com.pattern.proxy.entity.Person;
import com.pattern.proxy.invaction.Invaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static Person builder(Class classFile) throws Exception{


        //1.创建被监控实例对象
        Person baseService = (Person) classFile.newInstance();
        //2.创建一个通知对象
        InvocationHandler invaction = new Invaction(baseService);
        //3.向jvm申请负责监控obj对象指定的行为的监控对象（代理对象）
        /**
         *      loader:被监控对象隶属类文件在内存中的真实地址
         *      interfaces:被监控对象隶属类文件的实现接口
         *      h:监控对象发现小明需要执行被监控的行为，应该由哪一个通知对象去辅助
         */
        ClassLoader classLoader = baseService.getClass().getClassLoader();
        Class<?>[] interfaces = baseService.getClass().getInterfaces();
        Person $proxy =(Person) Proxy.newProxyInstance(baseService.getClass().getClassLoader(), baseService.getClass().getInterfaces(), invaction);




        return $proxy;


    }


}
