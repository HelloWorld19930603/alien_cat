package com.pattern.Facade.demo1;/**
 * @author sunzhuang
 * @date 2021年05月18日 9:32 上午
 */

/**
 * @author sunzhuang
 * @date 2021年05月18日 9:32 上午
 */


/**
 * 创建外观模式demo
 *
 * 通过一个接口类实现
 *
 *对一系列复杂系统的访问
 *
 * 屏蔽了对多系统访问的复杂性
 *
 */
public class FacadePatternDemo {

    public static void main(String[] args) {
        ShapeMaker shapeMaker = new ShapeMaker();

        shapeMaker.drawCircle();

        shapeMaker.drawSquare();

        shapeMaker.drawRectangle();
    }
}
