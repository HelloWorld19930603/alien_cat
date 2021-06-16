package com.aliencat.javabase.api.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenFrame extends JFrame implements java.awt.event.MouseListener { //实现MouseListener接口

    //截图的图片保存的目录
    private final String IMG_PATH = "C:\\Users\\WIN10\\Pictures\\";
    int x, y, w, h;
    boolean b = true;  //标记截取操作的次数，目的是为了是后面画的长方形唯一

    public ScreenFrame() {
        //setSize(300,300);
        try {//Robot会产生AWTException，需要进行捕获
            //setName("开始截图");

            Toolkit t = Toolkit.getDefaultToolkit();     //获取屏幕分辨率
            int ww = t.getScreenSize().width;
            int hh = t.getScreenSize().height;

            Robot r = new Robot();
            BufferedImage image = r.createScreenCapture(new Rectangle(0, 0, ww, hh));   //获取屏幕图片
            JLabel label = new JLabel(new ImageIcon(image));
            add(label);
            addMouseListener(this);
            setBounds(0, 0, ww, hh);
            //setResizable(false);
            setAlwaysOnTop(true);       //使得窗口一直浮在最上层
            setUndecorated(true);       //去掉边框
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ScreenFrame();
    }

    public void mouseClicked(MouseEvent e) {
        //鼠标按键在组件上单击（按下并释放）时调用。
        if (e.getClickCount() == 2) {  //当鼠标双击时候，响应此事件，作用是截取图片并保存

            try {
                Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(new Rectangle(x + 1, y + 1, w - 1, h - 1));
                //JLabel lab = new JLabel(new ImageIcon(image));
                ImageIO.write(image, "jpeg",
                        new File(IMG_PATH + (System.currentTimeMillis() / 1000) + "img.jpeg"));
                setVisible(false);
                Runtime.getRuntime()
                        .exec("c:\\windows\\System32\\rundll32.exe " +
                                "\"C:\\Program Files\\Windows Photo Viewer\\PhotoViewer.dll\", " +
                                "ImageView_Fullscreen c:\\a.jpg");               //这句代码貌似没起啥作用，暂时不清楚原因
                JOptionPane.showMessageDialog(null,
                        "图片存放路径为：C:\\Users\\WIN10\\Pictures",
                        "图片路径", 1);
                System.exit(0);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void mouseEntered(MouseEvent e) {
        //鼠标进入到组件上时调用。
    }

    public void mouseExited(MouseEvent e) {
        //鼠标离开组件时调用。

    }

    public void mousePressed(MouseEvent e) {
// 鼠标按键在组件上按下时调用。
        if (b) {           //获取鼠标按下是的坐标
            x = e.getX();
            y = e.getY();

//Graphics g = this.getGraphics();
//g.drawRect(x, y, -100, -100);
        }
    }

    public void mouseReleased(MouseEvent e) {
        //鼠标按钮在组件上释放时调用。
        //e.getSource();
        if (b) {

            Graphics g = getGraphics();   //获取图形上下文
            g.setColor(Color.green);      //切线颜色设置为绿色
            g.drawRect(x, y, e.getX() - x, e.getY() - y);
            //g.drawRect(x,y,e.getX()-x,e.getY()-y);
            w = e.getX() - x;    //获取鼠标释放时的坐标
            h = e.getY() - y;
            b = false;
        }
    }

}
