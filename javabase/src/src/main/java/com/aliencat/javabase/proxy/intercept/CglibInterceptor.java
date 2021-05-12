package com.aliencat.javabase.proxy.intercept;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibInterceptor implements MethodInterceptor {

    Object target;

    public CglibInterceptor(Object target) {
        this.target = target;
    }

    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("before " + method.getName());
        Object result = methodProxy.invoke(target, args);
        System.out.println("after " + method.getName());
        return "intercept " + result;
    }
}
