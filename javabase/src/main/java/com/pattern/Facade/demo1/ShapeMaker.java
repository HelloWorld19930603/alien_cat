package com.pattern.Facade.demo1;

/**
 * @author sunzhuang
 * @date 2021��05��18�� 9:07 ����
 *
 * ʹ�����õĹ�������
 * ִ�д�����ͬ����۵ķ�ʽ
 * ���β�ͬʵ�ֵĸ�����
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
