package com.pattern.template;

import com.pattern.template.impl.Drink;
import com.pattern.template.impl.Eat;
import com.pattern.template.tempinterface.MyTemp;

public class MainTest {
  public static void main(String[] args) {
    //
      System.out.println("---------------吃饭");
      MyTemp eat=new Eat();
      eat.service();

      System.out.println("---------------喝水");
      MyTemp drink=new Drink();
      drink.service();
  }
}
