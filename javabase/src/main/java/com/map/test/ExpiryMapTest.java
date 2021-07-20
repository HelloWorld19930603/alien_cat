package com.map.test;

import com.map.ExpiryMap;

public class ExpiryMapTest {
  public static void main(String[] args) {
    //
      ExpiryMap<String, String> expiryMap = new ExpiryMap<String,String>();
      String key="key";
      expiryMap.put(key,"value",1);
      System.out.println("时间内取值："+expiryMap.get(key));
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      System.out.println("过期后取值："+expiryMap.get(key));
  }
}
