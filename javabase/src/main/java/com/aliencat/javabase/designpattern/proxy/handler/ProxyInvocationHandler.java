package com.aliencat.javabase.designpattern.proxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler {

    Object target;

    public ProxyInvocationHandler(Object obj) {
        target = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("after " + method.getName());
        return "invoke " + result;
    }
}

