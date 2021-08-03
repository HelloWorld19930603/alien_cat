package com.aliencat.javabase.designpattern.bridge.gearbox;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动挡变速箱
 */
@Slf4j
public class AutoGearbox extends AbstractGearbox {


    @Override
    public void rotate() {
        log.info("Auto Gearbox rotate");
    }
}
