package com.spring.code.statictest;



public class InitClass {
    private static int a=0x1fc;
    private static char b;

  public static void main(String[] args) {
    // java执行默认初始化
        System.out.println("a:"+a);
        System.out.println("b:"+new String(b+""));
        System.out.println("a二："+Integer.toBinaryString(a));

      InitClass date=new InitClass();
      InitClass new1=date;
    System.out.println("date:"+date.toString());
    System.out.println("new1:"+new1.toString());
    System.out.println("----------------------------------");
    new1.a=1;
      System.out.println("date:"+date.toString());
      System.out.println("new1:"+new1.toString());
      System.out.println("date.a:"+date.a);
      System.out.println("new1.a:"+new1.a);
  }
}
//:~
