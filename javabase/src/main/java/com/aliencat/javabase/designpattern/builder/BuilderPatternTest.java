package com.aliencat.javabase.designpattern.builder;

/**
 * 建造者（Builder）模式的定义：
 * 指将一个复杂对象的构造与它的表示分离，使同样的构建过程可以创建不同的表示，这样的设计模式被称为建造者模式。
 * 它是将一个复杂的对象分解为多个简单的对象，然后一步一步构建而成。
 * 它将变与不变相分离，即产品的组成部分是不变的，但每一部分是可以灵活选择的。
 * <p>
 * 该模式的主要优点如下：
 * 封装性好，构建和表示分离。
 * 扩展性好，各个具体的建造者相互独立，有利于系统的解耦。
 * 客户端不必知道产品内部组成的细节，建造者可以对创建过程逐步细化，而不对其它模块产生任何影响，便于控制细节风险。
 * <p>
 * 其缺点如下：
 * 产品的组成部分必须相同，这限制了其使用范围。
 * 如果产品的内部变化复杂，如果产品内部发生变化，则建造者也要同步修改，后期维护成本较大。
 */
public class BuilderPatternTest {


    public static void main(String[] args) {
        //原始方式使用建造者模式
        Computor computor = Computor.builder()
                .screen("三星屏幕")
                .cpu("i5处理器")
                .mainboard("技嘉主板")
                .memory("金士顿内存")
                .keyboard("机械键盘")
                .mouse("惠普鼠标")
                .build();

        System.out.println(computor);

        //通过lombok使用建造者模式
        ComputorV2 computorV2 = ComputorV2.builder()
                .screen("苹果屏幕")
                .cpu("M1处理器")
                .mainboard("苹果主板")
                .memory("三星内存")
                .keyboard("无线键盘")
                .mouse("无线鼠标")
                .build();
        System.out.println(computorV2);
    }
}
