package com.spring.code.statictest;

public class Class2 {
  public static void main(String[] args) {
    //
//    System.out.println(Thread.currentThread().getId()+"程序2");
    System.getProperties().list(System.out);
    System.out.println(System.getProperty("user.name"));
  }
}
