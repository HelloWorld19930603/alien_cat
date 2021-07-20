package com.pattern.proxy.invaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Invaction implements InvocationHandler {

    private Object obj;
    //构造方法传入被代理的对象
    public Invaction(Object obj){
        this.obj=obj;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.确认当前被拦截的行为
        String methodName = method.getName();
        //2.根据被拦截行为的不同，执行不同的业务
        Object value ;
        if ("eat".equals(methodName)){
            wash();
            value= method.invoke(this.obj, args);
        }else {
            value= method.invoke(this.obj, args);
            wash();
        }
        return value;
    }


    public void wash(){
        System.out.println("---洗手---");
    }
}
