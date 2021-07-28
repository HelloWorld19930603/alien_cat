package com.aliencat.datastructure.map;

import org.junit.Test;

public class MapTest {

    /**
     * 普通测试
     */
    @Test
    public void test() {
        MyMap<String, String> MyHashMap = new MyHashMap<>();
        MyHashMap.put("name", "zhangsan");
        MyHashMap.put("height", "175cm");
        MyHashMap.put("age", "33");
        System.out.println("name:" + MyHashMap.get("name") + ",age:" + MyHashMap.get("age"));
    }

    /**
     * Hash冲突测试
     */
    @Test
    public void testHashConfilct() {
        MyHashMap<String, String> MyHashMap = new MyHashMap<>();
        MyHashMap.put("abc", "value1");
        MyHashMap.put("abc", "value2");
        System.out.println("abc:" + MyHashMap.get("abc"));
    }

    @Test
    public void testExpiryHashMap() {
        ExpiryHashMap<String, String> expiryHashMap = new ExpiryHashMap<>();
        String key = "key";
        expiryHashMap.put(key, "value", 888);  //888ms后过期
        System.out.println("有效期内取值：" + expiryHashMap.get(key));
        try {
            Thread.sleep(888);       //休眠888ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("过期后取值：" + expiryHashMap.get(key));
    }
}
