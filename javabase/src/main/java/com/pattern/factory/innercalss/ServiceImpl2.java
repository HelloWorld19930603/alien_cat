package com.pattern.factory.innercalss;

public class ServiceImpl2 implements Service {

    @Override
    public void method1() {
        System.out.println("ServiceImpl2:method1");
    }

    @Override
    public void method2() {
        System.out.println("ServiceImpl2:method2");
    }

    public static ServiceFactory serviceFactory=new ServiceFactory() {
        @Override
        public Service getServer() {
            return new ServiceImpl2();
        }
    };


}
