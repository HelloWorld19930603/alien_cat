package com.aliencat.javabase.api.string;

import org.junit.Test;

/**
 * String 常量池相关特性的测试
 * 注意：本测试使用的版本为JDK8,
 * 在JDK9中对于字符串的操作又进行了其它优化，这里不作讨论
 */
public class StringPoolTest {

    @Test
    public void test1() {
        String s1 = "abc"; // 字面量定义的方式，"abc" 存储在字符串常量池中
        String s2 = "abc"; // s1,s2 指向同一个 abc
        String s3 = new String("abc");  // s3 在堆中创建了"abc"
        // intern():判断字符串常量池中是否存在 "abc" 值，如果存在，则返回常量池中 "abc" 的地址；
        // 如果字符串常量池中不存在 "abc"，则在常量池中加载一份 "abc"，并返回次对象的地址。
        String s4 = s3.intern();

        System.out.println(s1 == s2); // true
        System.out.println(s1 == s3); // false
        System.out.println(s1 == s4); // true

    }

    @Test
    public void test2() {
        String s1 = "abc";
        String s2 = "abc";
        String s3 = s2;
        s2 += "def";
        System.out.println(s1);           // abc
        System.out.println(s2);           // abcdef
        System.out.println(s3);           // abc
        System.out.println(s1 == s2);  // false
        System.out.println(s1 == s3);  // true
    }

    @Test
    public void test3() {
        String s1 = "abc";
        String s2 = "dbc";
        String s3 = s2.replace('d', 'a'); //replace方法会在堆中创建字符串
        System.out.println(s1 == s3);  // false
    }


    @Test
    public void test4() {
        String s1 = "abc";
        char[] chars = s1.toCharArray();
        String s2 = String.valueOf(chars);
        System.out.println(s1 == s2);  // false
    }


    /**
     * 字符串拼接测试
     */
    @Test
    public void test5() {
        String s1 = "a" + "b" + "c";     // 编译期优化：等同于 "abc"
        String s2 = "abc";                 // "abc" 一定是放在字符串常量池中，将此地址赋给 s2
        String s3 = "c";
        String s4 = "ab" + s3;        //s4需要运行期间才能确定，所以最后s4会在堆中创建
        System.out.println(s1 == s2);        // true
        System.out.println(s1 == s4);        // false
        System.out.println(s1.equals(s4));  // true
    }

    @Test
    public void test6() {
        String s1 = "ab" + "c";     // 编译期优化：等同于 "abc"
        String s2 = "ab" + new String("c");  // 这里会使用StringBuilder拼接，在堆中创建
        System.out.println(s1 == s2);        // false
    }

    /**
     * 1. 字符串拼接操作不一定使用的是 StringBuilder
     * 2. 如果拼接符号左右两边都是字符串常量或常量引用，则仍然使用编译期优化，即非 StringBuilder 的方式。
     * 3. 针对于 final 修饰类、方法、基本数据类型、引用数据类型的量的结构时，能使用上 final 的时候建议使用上。
     */
    @Test
    public void test7() {
        final String s1 = "a";
        final String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2;
        System.out.println(s3 == s4);    // true
    }


    /**
     * 面试题
     */
    @Test
    public void test8() {
        /**
         * new String(“ab”)会创建几个对象？
         * 2 个
         * 一个对象是：new 关键字在堆空间创建的
         * 另一个对象是：字符串常量池中的对象 “ab”。 字节码指令：ldc
         * 但注意：str 此时指向的是堆空间当中的对象
         */
        String s1 = new String("ab");
        /**
         * new String(“c”) + new String(“d”)呢？
         * 6个
         * 对象1：new StringBuilder()，变量拼接 “+” 操作肯定有 StringBuilder
         * 对象2： new String(“a”)
         * 对象3： 常量池中的"a"
         * 对象4： new String(“b”)
         * 对象5： 常量池中的"b"
         * 对象6 ：new String(['a','b'])，通过字符数组创建的String并没有在字符串常量池当中生成 ab。
         */
        String s2 = new String("c") + new String("d");

        /**
         * 此时 "ab" 会创建几个对象？
         * 0个
         * 因为此时字符串常量池已经有"ab"了，所以会直接返回已经有的
         */
        String s3 = "ab";
    }

}
