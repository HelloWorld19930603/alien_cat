package com.aliencat.javabase.designpattern.bridge.car;

import com.aliencat.javabase.designpattern.bridge.gearbox.AbstractGearbox;

/**
 * 抽象车
 */
public abstract class AbstractCar {

    //变速箱
    protected AbstractGearbox abstractGearbox;

    public abstract void run();

    public void setTransmission(AbstractGearbox abstractGearbox) {
        this.abstractGearbox = abstractGearbox;
    }

}
