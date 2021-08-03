package com.aliencat.javabase.designpattern.bridge.car;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GermanCar extends AbstractCar {

    @Override
    public void run() {
        abstractGearbox.rotate();
        log.info("JapaneseCar is running");
    }
}
