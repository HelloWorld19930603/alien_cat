package com.aliencat.javabase.designpattern.proxy;

import com.aliencat.javabase.designpattern.proxy.bean.CglibDemo;
import com.aliencat.javabase.designpattern.proxy.intercept.CglibInterceptor;
import net.sf.cglib.proxy.Enhancer;

public class CglibProxy {


    private Object target;

    public static void main(String[] args) {
        CglibDemo cglibDemo = (CglibDemo) new CglibProxy().getInstance(new CglibDemo());
        System.out.println(cglibDemo.print());
    }

    public Object getInstance(final Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(new CglibInterceptor(target));
        return enhancer.create();
    }
}
