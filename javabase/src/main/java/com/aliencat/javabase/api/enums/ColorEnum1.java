package com.aliencat.javabase.api.enums;

public enum ColorEnum1 {
    RED("红色"),GREEN("绿色"),BLUE("蓝色");

    String name;

    ColorEnum1(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
