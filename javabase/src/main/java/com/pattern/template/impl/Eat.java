package com.pattern.template.impl;

import com.pattern.template.tempinterface.MyTemp;

//1.定义吃饭的方法，需要执行的步骤
//2.调用模板的服务方法，即可按照步骤执行
public class Eat extends MyTemp {
    @Override
    public void step1() {
    System.out.println("洗手");
    }

    @Override
    public void step2() {
    System.out.println("吃饭");
    }

    @Override
    public void step3() {
    System.out.println("刷晚");
    }
}
