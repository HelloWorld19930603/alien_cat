package com.aliencat.algorithm.math;

import java.util.Scanner;

public class Gcd {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);//以空格作为分隔符
        System.out.println("请输入a:");
        int a = scan.nextInt();
        System.out.println("请输入b:");
        int b = scan.nextInt();
        int m;
        for (; ; ) {
            m = a % b;
            if (m == 0)
                break;
            else {
                a = b;
                b = m;
            }
        }
        System.out.println("最大公约数为：" + b);
    }
}
