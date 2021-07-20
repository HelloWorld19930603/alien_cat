package com.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//小孩接口
interface Children {
    void eatChicken();

    void eatFruits();

    void cry();
}

public class ProxyMain2 {

    public static void main(String[] args) {
        Children baby = new Baby();
        Dad dad = new Dad(baby);
        //使用java的动态代理方式创建代理
        Children $dad = (Children) Proxy.newProxyInstance(baby.getClass().getClassLoader(), baby.getClass().getInterfaces(), dad);
        $dad.eatChicken();
        $dad.eatFruits();
        $dad.cry();
    }
}

//必须要有孩子的爸爸，因为需要代理孩子
class Dad implements InvocationHandler {
    private Children children;

    public Dad(Children children) {
        this.children = children;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //判断孩子是否要吃东西
        if (method.getName().indexOf("eat") != -1) {
            wash();
            method.invoke(children, args);//使用反射的方式执行方法
        }

        return null;
    }

    private void wash() {
        System.out.println("洗手");

    }
}

class Baby implements Children {
    @Override
    public void eatChicken() {
        System.out.println("吃鸡肉");
    }

    @Override
    public void eatFruits() {
        System.out.println("吃水果");
    }

    @Override
    public void cry() {
        System.out.println("哭");
    }
}