package com.aliencat.javabase.designpattern.bridge.gearbox;

import lombok.extern.slf4j.Slf4j;

/**
 * 手动档变速箱
 */
@Slf4j
public class ManualGearbox extends AbstractGearbox {

    @Override
    public void rotate() {
        log.info("Manual Gearbox rotate");
    }
}
