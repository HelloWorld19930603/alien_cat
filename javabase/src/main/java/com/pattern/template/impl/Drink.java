package com.pattern.template.impl;

import com.pattern.template.tempinterface.MyTemp;

public class Drink extends MyTemp {
    @Override
    public void step1() {
    System.out.println("拿被子");
    }

    @Override
    public void step2() {
    System.out.println("喝水");
    }

    @Override
    public void step3() {
    System.out.println("放杯子");
    }
}
