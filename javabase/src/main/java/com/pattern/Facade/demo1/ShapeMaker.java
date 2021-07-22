package com.pattern.Facade.demo1;

/**
 * @author sunzhuang
 * @date 2021年05月18日 9:07 上午
 *
 * 使用内置的工厂方法
 * 执行创建不同的外观的方式
 * 屏蔽不同实现的复杂性
 */
public class ShapeMaker {

    private Shape circle;

    private Shape square;

    private Shape rectangle;

    public ShapeMaker(){
        circle=new Circle();
        square=new Square();
        rectangle=new Rectangle();
    }

    public void drawCircle(){
        circle.draw();
    }

    public void drawSquare(){
        square.draw();
    }

    public void drawRectangle(){
        rectangle.draw();
    }
}
