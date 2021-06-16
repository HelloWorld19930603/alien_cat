package com.aliencat.javabase.api.swing.tools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 */
public class Tools {

    //获取的屏幕 宽度（screenWidth） 和 高度（screenHeight）
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    //系统剪切板
    public static Clipboard clipboard;

    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();
        clipboard = toolkit.getSystemClipboard();
    }

    /**
     * 获取当前时间
     *
     * @return String
     */
    public static String getLocalDate() {
        return new SimpleDateFormat("yyyymmddhhss").format(new Date());
    }

    //
    public static final Rectangle SCREEN_RECTANGLE = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    public static final Color RECT_COLOR = Color.BLUE;

}
