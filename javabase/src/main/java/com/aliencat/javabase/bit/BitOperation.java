package com.aliencat.javabase.bit;

import org.junit.Test;

/**
 * 程序中的所有数在计算机内存中都是以二进制的形式储存的。
 * 位运算就是直接对整数在内存中的二进制位进行操作。
 */
public class BitOperation {

    @Test
    public void test1() {
        int rgb = 1000;
        System.out.println((rgb / 256) % 256);

        System.out.println((rgb >> 8) & 0xFF);
    }

    /**
     * AND（&）是一个二进制运算符，用于比较两个长度相等的操作数。
     * 操作数从可读形式转换为二进制表示形式。对于每个位，该操作检查两个操作数中的两个位是否都是1。
     * 如果是，则该位在答案中设置为1。否则，相应的结果位设置为0。
     */
    @Test
    public void test2() {
        int x = 9;
        int y = 5;
        System.out.println("位运算操作 (" + x + " & " + y + ") = " + (x & y));
    }

    /**
     * OR运算符（|）是一个二进制运算符，它接受两个等长的操作数，但将它们与AND进行相反的比较；
     * 如果相应的位为1，则答案为1。否则，答案为0。
     */
    @Test
    public void test3() {
        int x = 9;
        int y = 5;
        System.out.println("位运算操作 (" + x + " | " + y + ") = " + (x | y));
    }

    /**
     * 按位异或运算（^）是“异或”的缩写，是一个二进制运算符，它接受两个输入参数并比较每个对应的位。
     * 如果位是相反的，则结果在该位等于1。如果一样则返回0。
     */
    @Test
    public void test4() {
        int x = 9;
        int y = 5;
        System.out.println("位运算操作 (" + x + " ^ " + y + ") = " + (x ^ y));
    }

    /**
     * NOT（~）或有时称为位补码运算符，是一种一元运算，它接受单个输入，并将二进制表示中的每一位换成相反的值。
     */
    @Test
    public void test5() {
        int x = 9;
        System.out.println("位运算操作 ( ~" + x + ") = " + (~x));
    }

    /**
     * 位移位是一种按位操作，其中移动一系列位的顺序以有效地执行数学运算。
     * 位移位将数字二进制表示形式中的每个数字向左或向右移动第二个操作数指定的空格数。
     * 左移位：<<是左移位运算符，满足逻辑和算术移位的需要。
     * 算术/有符号右移：>>是算术（或有符号）右移运算符。
     * 逻辑/无符号右移：>>>是逻辑（或无符号）右移运算符。
     */
    @Test
    public void test6() {
        int i = 1;
        System.out.println(i << 9);
        System.out.println(i >> 6);
    }

    /**
     * 将字符转换为大写或小写
     * 小写字母和大写字母的二进制表示几乎相同，除了第6位不同。
     */
    @Test
    public void testToggleCase() {
        char c = 'c';
        System.out.println((char) (c ^ 32));  //转大写
        c = 'C';
        System.out.println((char) (c ^ 32)); //转小写
    }

    /**
     * 检查数字奇偶性
     * 任何时候，奇数最后一位bit为1，1的二进制为 1
     * 当x&1时候只要判断结果等于1则，x最后一个bit就是为1，此时为奇数。
     */
    @Test
    public void testParity() {
        int x = 111;
        System.out.println(x + "为偶数：" + ((x & 1) == 0));
        System.out.println(x + "为奇数：" + ((x & 1) == 1));
    }
}
