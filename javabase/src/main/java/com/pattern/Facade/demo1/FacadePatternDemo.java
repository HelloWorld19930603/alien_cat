package com.pattern.Facade.demo1;/**
 * @author sunzhuang
 * @date 2021��05��18�� 9:32 ����
 */

/**
 * @author sunzhuang
 * @date 2021��05��18�� 9:32 ����
 */


/**
 * �������ģʽdemo
 *
 * ͨ��һ���ӿ���ʵ��
 *
 *��һϵ�и���ϵͳ�ķ���
 *
 * �����˶Զ�ϵͳ���ʵĸ�����
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
