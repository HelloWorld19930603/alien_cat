package com.aliencat.javabase.api.enums;

public enum ColorEnum3 {
    RED("红色") {
        public void printColor() {
            isRed();
        }

        public void isRed(){
            System.out.println("这是一个红色");
        }
    },
    GREEN("绿色") {
        public void printColor() {
            System.out.println("最富有活力的颜色：" + name);
        }
    },
    BLUE("蓝色") {
        public void printColor() {
            System.out.println("这是天空的颜色:" + name);
        }
    };

    String name;

    ColorEnum3(String name) {
        this.name = name;
    }

    public abstract void printColor();

}
