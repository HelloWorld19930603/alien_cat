package com.spring.code.statictest;

public class Class1 {
  public static void main(String[] args) {
    //
    System.out.println(args.toString());
    System.out.println(Thread.currentThread().getId()+"程序1");
    String[] args2=null;
    Class2.main(args2);
  }
}
