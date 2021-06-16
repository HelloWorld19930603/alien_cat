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
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    //系统剪切板
    public static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

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
