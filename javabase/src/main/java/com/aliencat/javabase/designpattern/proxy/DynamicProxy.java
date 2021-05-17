package com.aliencat.javabase.designpattern.proxy;

import com.aliencat.javabase.designpattern.proxy.interfaces.IDemo;
import com.aliencat.javabase.designpattern.proxy.bean.Demo;
import com.aliencat.javabase.designpattern.proxy.handler.ProxyInvocationHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxy {


    public static void main(String[] args) throws Exception {
        IDemo dem = (IDemo) getProxyObject(IDemo.class);
        System.out.println(dem.print());
    }

    public static Object getProxyObject(Class clazz) throws Exception {
        ProxyInvocationHandler handler = new ProxyInvocationHandler(new Demo());
        Class c = Proxy.getProxyClass(clazz.getClassLoader(), clazz);
        Constructor con = c.getConstructor(InvocationHandler.class);
        Object o = con.newInstance(handler);
        return o;
    }

}
