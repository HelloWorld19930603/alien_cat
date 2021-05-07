package com.aliencat.javabase.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class ProxyDemo {


    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        org.example.proxy.ProxyInvocationHandler handler = new org.example.proxy.ProxyInvocationHandler();
        Class c = Proxy.getProxyClass(Demo.class.getClassLoader(),new Class[]{IDemo.class});
        Constructor con = c.getConstructor(new Class[]{InvocationHandler.class});
        Object o = con.newInstance(handler);
        IDemo demo = (IDemo) o;
        demo.print();
    }


}
