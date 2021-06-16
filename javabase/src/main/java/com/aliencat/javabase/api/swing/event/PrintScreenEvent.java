package com.aliencat.javabase.api.swing.event;

import com.aliencat.javabase.api.swing.PrintScreen;

import java.awt.*;

/**
 * PrintScreen事件类
 * 可以重事件里获取到选区的图片，当点击菜单完成时候产生此事件。
 */
public class PrintScreenEvent {

    //存截取的图片
    private Image screenImage;


    /**
     * 构造方法，用来初始化事件里的数据。
     */
    public PrintScreenEvent(PrintScreen ps) {
        screenImage = ps.getScreenImage();
    }


    /**
     * 获取屏幕截图
     */
    public Image getScreenImage() {
        return screenImage;
    }
}
