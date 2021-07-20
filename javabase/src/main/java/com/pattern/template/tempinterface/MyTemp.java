package com.pattern.template.tempinterface;

public abstract class MyTemp {

  public abstract void step1();

  public abstract void step2();

  public abstract void step3();
  // 如果提供一项完整的服务,需要三个步骤，都可以调用实现这个方法

  public void service() {
    step1();
    step2();
    step3();
  }
}
