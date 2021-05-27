package com.aliencat.javabase.api.enums;

interface ColorInfo {
    public void printColor();
}


public enum ColorEnum2 implements ColorInfo {
    RED("红色") {
        public void printColor() {
            System.out.println("最喜欢的颜色是"+name);
        }
    },
    GREEN("绿色") {
        public void printColor() {
            System.out.println("最富有活力的颜色："+name);
        }
    },
    BLUE("蓝色") {
        public void printColor() {
            System.out.println("这是天空的颜色:"+name);
        }
    };

    String name;

    ColorEnum2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
