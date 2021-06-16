package com.aliencat.javabase.api.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * 全屏截图
 */
public class ScreenAll {
    private int x, y;
    //图片保存路径
    private final String IMG_PATH = "c:\\";

    public ScreenAll() {
        try {
            Toolkit tool = Toolkit.getDefaultToolkit();
            x = tool.getScreenSize().width;
            y = tool.getScreenSize().height;

            Robot r = new Robot();
            BufferedImage image = r.createScreenCapture(new Rectangle(0, 0, x, y));
            //生成保存的图片名
            String fineName = (System.currentTimeMillis() / 1000) + "SreenshotAll.jpg";
            ImageIO.write(image, "jpeg", new File(IMG_PATH + fineName));
            Runtime.getRuntime()
                    .exec("c:\\windows\\System32\\rundll32.exe \"C:\\Program Files\\Windows Photo Viewer\\PhotoViewer.dll\", ImageView_Fullscreen c:\\a.jpg");
            JOptionPane.showMessageDialog(null, "图片存放路径为：" + IMG_PATH + fineName, "图片路径", 1);
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
