package com.aliencat.javabase.api.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenAll {
    private int x, y;

    public ScreenAll() {
        try {
            Toolkit tool = Toolkit.getDefaultToolkit();
            x = tool.getScreenSize().width;
            y = tool.getScreenSize().height;

            Robot r = new Robot();
            BufferedImage image = r.createScreenCapture(new Rectangle(0, 0, x, y));
            ImageIO.write(image, "jpeg", new File("c:\\SreenshotAll.jpg"));
            Runtime.getRuntime()
                    .exec("c:\\windows\\System32\\rundll32.exe \"C:\\Program Files\\Windows Photo Viewer\\PhotoViewer.dll\", ImageView_Fullscreen c:\\a.jpg");
            JOptionPane.showMessageDialog(null, "图片存放路径为：c:\\SreenshotAll.jpg", "图片路径", 1);
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
