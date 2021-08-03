package com.aliencat.javabase.designpattern.bridge;

import com.aliencat.javabase.designpattern.bridge.car.AbstractCar;
import com.aliencat.javabase.designpattern.bridge.car.ChineseCar;
import com.aliencat.javabase.designpattern.bridge.car.JapaneseCar;
import com.aliencat.javabase.designpattern.bridge.gearbox.AbstractGearbox;
import com.aliencat.javabase.designpattern.bridge.gearbox.AutoGearbox;
import com.aliencat.javabase.designpattern.bridge.gearbox.ManualGearbox;

/**
 * 桥接模式（Bridge Pattern）
 * 将抽象部分与它的实现部分分离，使它们都可以独立地变化。
 * 实现系统可从多种维度分类，桥接模式将各维度抽象出来，各维度独立变化，
 * 之后可通过聚合，将各维度组合起来，减少了各维度间的耦合。
 */
public class BridgeCarTest {

    /**
     * 假设有三系车：国产车、德系车、日系车，分别有手动档和自动挡，不采用桥接模式的话就要实现3 * 2 种类
     * 使用了桥接模式则只需要3 + 2种类；
     * 如果此时每系车再增加一种变速箱的车，比如手自一体，则不采用桥接模式就需要实现3 * 3种类，
     * 而采用桥接模式后则只需要多添加一个类即可。
     */
    public static void main(String[] args) {
        //自动挡日系车
        AbstractGearbox auto = new AutoGearbox();
        AbstractCar car = new JapaneseCar();
        car.setTransmission(auto);
        car.run();

        //手动档国产车
        AbstractGearbox manual = new ManualGearbox();
        car = new ChineseCar();
        car.setTransmission(manual);
        car.run();
    }
}
