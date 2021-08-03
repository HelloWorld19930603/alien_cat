package com.aliencat.javabase.designpattern.bridge.car;

import lombok.extern.slf4j.Slf4j;

/**
 * 日系车
 */
@Slf4j
public class JapaneseCar extends AbstractCar {

    @Override
    public void run() {
        abstractGearbox.rotate();
        log.info("JapaneseCar is running");
    }

}
