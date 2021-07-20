package com.pattern.factory.main;

import com.pattern.factory.factory.OperationFactory;

import java.util.Scanner;

/**
 * 工厂模式
 *
 * @author Administrator
 * <p>
 * 通过不同的条件，创建出不同功能的类。降低代码修改的复杂度。
 * <p>
 * <p>
 * 解决的问题：将方法之间的不同功能解耦合，目的是为了减少业务变更引起的
 * 修改大量的代码，
 */
public class MyOperation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printString("请输入第一个数：");
        double numberA = scanner.nextDouble();
        printString("请输入操作符+或-：");
        String ope = scanner.next();
        printString("请输入第二个数：");
        double numberB = scanner.nextDouble();
        printString("结果为" + OperationFactory.getResult(numberA, numberB, ope));
    }

    private static void printString(String string) {
        // TODO Auto-generated method stub
        System.out.println(string);
    }


}
