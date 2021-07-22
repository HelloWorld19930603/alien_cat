package com.pattern.factory.innercalss;

public class FactoryMain {

  public static void main(String[] args) {
    //工厂模式测试

      Factory factory = new Factory(ServiceImpl1.serviceFactory);
      Service server = factory.getServer();
      server.method1();
  }
}
