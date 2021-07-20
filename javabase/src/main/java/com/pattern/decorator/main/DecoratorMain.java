package com.pattern.decorator.main;

import com.pattern.decorator.Component;
import com.pattern.decorator.ComponentImpl;
import com.pattern.decorator.DecoratorA;
import com.pattern.decorator.DecoratorB;

/**
 * 装饰者模式
 * 动态的给一个对象添加一些额外的职能，就增加功能而言，装饰者模式比生成子类更加灵活。
 * 额外的职能可以是属性也可以方法。
 * <p>
 * <p>
 * <p>
 * java特性：
 * 继承 ★★★★★
 * 重写 ★★★☆☆
 * 抽象 ★☆☆☆☆
 * 模式重点：
 * 1.将一个对象的方法和类进行解耦。可以给一个类动态的增加新的行为。
 * 2.将类和方法服务进行解耦有什么好处？
 * 3.子类在重写父类的方法的同时先执行一遍父类的方法。（super.operation（））
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * 适用场景：1.需要灵活或者频繁添加或者移除功能的时候
 */
public class DecoratorMain {
    public static void main(String[] args) {

        /**
         * 1.Component  存在的意见：让所有类继承 统一类类型
         * 声明基础的ComponentImpl类，可以理解为需要添加装饰的主体
         *
         * 需要添加装饰的主题可以是一个方法或者多个方法
         *
         * 也可以是有返回值的，可以对返回值进行不同的更改。（返回值可以是一个类，在不同的装饰类更改不同的属性）
         */
        Component component = new ComponentImpl();

        /**
         * 添加装饰 （属性 方法）
         */
        DecoratorA decoratorA = new DecoratorA();
        decoratorA.setComponent(component);
        decoratorA.setAddState("捡了一双翅膀!");


        DecoratorB decoratorB = new DecoratorB();
        decoratorB.setComponent(decoratorA);

        DecoratorA decoratorC = new DecoratorA();
        decoratorC.setComponent(decoratorB);
        decoratorC.setAddState("又捡了一双翅膀!");

        decoratorC.operation();

    }
}
