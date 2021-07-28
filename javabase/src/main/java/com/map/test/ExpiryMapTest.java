package com.map.test;

import com.map.ExpiryHashMap;

public class ExpiryMapTest {
    public static void main(String[] args) {
        //
        ExpiryHashMap<String, String> expiryHashMap = new ExpiryHashMap<String, String>();
        String key = "key";
        expiryHashMap.put(key, "value", 888);
        System.out.println("有效期内取值：" + expiryHashMap.get(key));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("过期后取值：" + expiryHashMap.get(key));
    }
}
