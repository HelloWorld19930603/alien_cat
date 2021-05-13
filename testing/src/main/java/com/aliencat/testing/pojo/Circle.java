package com.aliencat.testing.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class Circle {
    private double radius;
    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }
}