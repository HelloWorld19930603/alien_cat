package com.aliencat.javabase.designpattern.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 * 借助lombok使用建造者模式创建一台电脑
 * 代码简洁不少，就是需要引入lombok插件和依赖，对代码有侵入性
 */
@Builder
@ToString
@AllArgsConstructor
public class ComputorV2 {

    private String cpu;
    private String screen;
    private String memory;
    private String mainboard;
    private String mouse;
    private String keyboard;

    public ComputorV2() {
        throw new RuntimeException("材料不足，无法建造");
    }


}
