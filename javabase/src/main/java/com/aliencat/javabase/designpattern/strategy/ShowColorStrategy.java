package com.aliencat.javabase.designpattern.strategy;

import com.aliencat.javabase.api.enums.ColorEnum;

public enum ShowColorStrategy {
    EXPRESS {
        @Override
        public void show(ColorEnum ce) {
            System.out.println("color is " + ce.name());
        }
    },
    NORMAL {
        @Override
        public void show(ColorEnum ce) {
            System.out.println("color's number :" + ce.ordinal());
        }
    };

    public abstract void show(ColorEnum ce);
}
