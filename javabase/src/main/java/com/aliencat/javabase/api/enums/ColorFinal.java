package com.aliencat.javabase.api.enums;

//不使用枚举示例
public class ColorFinal {
    public String name;
    public static final ColorFinal RED = new ColorFinal("红色");
    public static final ColorFinal GREED = new ColorFinal("绿色");
    public static final ColorFinal BLUE = new ColorFinal("蓝色");

    public ColorFinal(String name){
        this.name = name;
    }
}