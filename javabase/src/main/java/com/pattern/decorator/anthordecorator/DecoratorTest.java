package com.pattern.decorator.anthordecorator;

import java.util.ArrayList;

public class DecoratorTest {

  public static void main(String[] args) {
    //
      Iphone iphone6 = new Iphone6();
      Iphone  iphoneDecorator = new IphoneDecorator(iphone6);
      Iphone ligntDecorator = new LigntDecorator(iphoneDecorator);
      ligntDecorator.call();
      ArrayList<Long> longs = new ArrayList<Long>();
      longs.add(1L);
      int i = longs.indexOf(1L);
    System.out.println(i);
  }
}
