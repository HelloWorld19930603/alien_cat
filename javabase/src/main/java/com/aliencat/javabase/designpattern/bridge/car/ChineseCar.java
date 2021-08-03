package com.aliencat.javabase.designpattern.bridge.car;

import lombok.extern.slf4j.Slf4j;

/**
 * 国产车
 */
@Slf4j
public class ChineseCar extends AbstractCar {

    @Override
    public void run() {
        abstractGearbox.rotate();
        log.info("ChineseCar is running");
    }
}
