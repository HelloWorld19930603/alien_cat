package com.aliencat.javabase.api.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 截图应用入口
 */
public class ScreenshotMain extends JFrame {
    JMenuBar menuBar;//初始化菜单栏
    JMenu menu;
    JMenuItem item1, item2, item3, item4;

    public ScreenshotMain() {

        Container con = getContentPane();
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        item1 = new JMenuItem("截图");
        item2 = new JMenuItem("查看图片");
        item3 = new JMenuItem("截取全屏");
        item4 = new JMenuItem("退出");
        setName("图片截取和查看软件");
        setBounds(200, 199, 300, 250);
        JButton button = new JButton("开始截图");
        menu.add(item1);
        menu.add(item2);
        menu.add(item3);
        menu.add(item4);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        add(button);
        setVisible(true);
        setResizable(false);
        button.addActionListener(new ActionListener() {//按钮事件，采取匿名内部类方法，调用ScreenCaptureTool()操作
            public void actionPerformed(ActionEvent e) {
                //repaint(1000, 300, 400, 300, 400) ;
                setVisible(false);
                // try{
                //Thread.sleep(100);
                // }catch(Exception ee){}
                new ScreenCaptureTool();
            }
        });

        item1.addActionListener(new ActionListener() {//第一个菜单选项截图的事件监听
            public void actionPerformed(ActionEvent e) {
                setVisible(false);//隐藏主界面
                new ScreenCaptureTool();
            }
        });
        item2.addActionListener(new ActionListener() { //第二个菜单选项查看图片的事件监听

            public void actionPerformed(ActionEvent e) {
                String[] s = {""};
                new ImageViewer().main(s);
            }
        });
        item3.addActionListener(new ActionListener() {//第三个菜单选项截取全屏的事件监听
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new ScreenAll();
            }
        });
        item4.addActionListener(new ActionListener() {//第四个菜单选项退出的事件监听
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {//程序运行入口
        new ScreenshotMain();
        //System.exit(0);
    }
}