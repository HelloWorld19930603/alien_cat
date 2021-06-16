package com.aliencat.javabase.api.swing;


import com.aliencat.javabase.api.swing.event.PrintScreenEvent;
import com.aliencat.javabase.api.swing.event.PrintScreenListener;
import com.aliencat.javabase.api.swing.tools.FileFilter;
import com.aliencat.javabase.api.swing.tools.MouseTransferable;
import com.aliencat.javabase.api.swing.tools.Tools;
import com.aliencat.javabase.api.swing.util.Rect;
import com.aliencat.javabase.api.swing.util.ScreenGamer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 截图JDialog窗体,程序主要框架，用于显示Robot截取屏幕图片显示
 * JDialog容器可以解决窗体焦点问题 和 任务栏图标显示问题
 */
public class PrintScreen extends JDialog {

    private static final long serialVersionUID = 1L;


    //屏幕尺寸
    private int screenWidth = Tools.SCREEN_WIDTH;
    private int screenHeight = Tools.SCREEN_HEIGHT;

    private BufferedImage imageCache;//截图缓存对象

    //截图帮助工具
    private Rect r = new Rect(this);//矩形框对象
    private ScreenGamer sg = new ScreenGamer();//放大镜头
    private PrintScreenListener listener;//截图事件监听器

    private JFileChooser jc;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始截图入口程序
     * 右键选择菜单
     * 点击完成：复制到剪贴板
     * 点击保存：自定义保存路径
     */
    public static void main(String[] args) {
        new PrintScreen().start();
    }


    /**
     * PrintScreen构造方法
     * 包含JPanel内部类(绘制截图相关内容的主要方法paint())，注册鼠标事件，以及窗体相关属性设置。
     */
    public PrintScreen() {
        //不显示窗体装饰
        this.setUndecorated(true);
        this.setBounds(0, 0, screenWidth, screenHeight);

        this.setContentPane(new JPanel() {
            private static final long serialVersionUID = 1L;

            public void paint(Graphics g) {
                super.paint(g);
                if (imageCache == null) { //判断缓存是否被占用
                    try {
                        imageCache = new Robot().createScreenCapture(Tools.SCREEN_RECTANGLE);//截取屏幕图片
                    } catch (AWTException e) {
                        System.out.println("Robot class create picture cache failed!");
                    }
                }
                g.drawImage(imageCache, 0, 0, screenWidth, screenHeight, null);//绘制截图
                r.drawRect(g);//绘制矩形
                sg.drawGamer(g, imageCache);//绘制放大镜，在这里没有做判断，所以在选定截区时就会显示
            }
        });


        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                r.mousePressed(e);//传递事件给选区
                if (e.getClickCount() == 2) {
                    copyInShearPlate();//复制到剪切板
                }
            }

            public void mouseReleased(MouseEvent e) {//鼠标按键弹起
                r.mouseReleased(e);

                if (e.getButton() == 3) {//弹出菜单
                    showMouseMenu(e.getX(), e.getY());
                }
            }

        });

        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                r.mouseDragged(e);
                sg.mouseDragged(e);
                repaint();//拖拽时必须重绘,如果是局部重绘，就会出现影子显现,只有牺牲效率了
            }

            public void mouseMoved(MouseEvent e) {
                r.mouseMoved(e);//矩形选区鼠标移动事件

            }
        });
        /**
         * 键盘事件
         * */
        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 27) {//ESC键
                    finishAndinitialization();//初始化
                }
            }
        });

    }

    /**
     * 显示鼠标右键菜单
     *
     * @param x 菜单X坐标
     * @param y 菜单Y坐标
     */
    public void showMouseMenu(int x, int y) {
        JPopupMenu p = new JPopupMenu();

        JMenuItem complete = new JMenuItem("完成", null);
        JMenuItem save = new JMenuItem("保存", null);
        JMenuItem exit = new JMenuItem("退出", null);

        complete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyInShearPlate();//复制到剪切板
            }
        });
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveImageFile();
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finishAndinitialization();
            }
        });

        p.add(complete);
        p.add(save);
        p.addSeparator();
        p.add(exit);

        this.add(p);
        p.show(this, x, y);//显示菜单到指定坐标
    }

    public void copyInShearPlate() {
        Tools.clipboard.setContents(new MouseTransferable(getScreenImage()), null);//拷贝到剪切板里
        if (listener != null) {//判断有木有监听器 (嵌入其他GUI项目里就必须有)
            listener.PrintScreenSaved(new PrintScreenEvent(this));//调用保存方法
        }
        finishAndinitialization();//回收资源
    }


    /**
     * 获取截图内容
     *
     * @return Image 返回矩形选区里的图像信息
     */
    public Image getScreenImage() {
        Rectangle re = r.getRect();
        return imageCache.getSubimage(re.x, re.y, re.width, re.height);
    }


    /**
     * 保存文件（自定义文件保存位置方法）
     */
    public void saveImageFile() {
        this.setAlwaysOnTop(false);//取消JFrame窗体置顶
        jc = new JFileChooser();
        jc.setDialogTitle("保存截图");
        //保存文件格式
        jc.addChoosableFileFilter(new FileFilter("bmp", ".bmp (*.bmp)"));
        jc.addChoosableFileFilter(new FileFilter("gif", ".gif (*.gif)"));
        jc.addChoosableFileFilter(new FileFilter("png", ".png (*.png)"));
        jc.addChoosableFileFilter(new FileFilter("jpg", ".jpg (*.jpg; *.jpeg; *.jpe)"));

        //不显示所有文件
        jc.setAcceptAllFileFilterUsed(false);

        //显示保存文件选择器窗口
        int result = jc.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) { // 用户点击了“确定”按钮
            File file = jc.getSelectedFile(); //获得文件名
            String ends = ((FileFilter) jc.getFileFilter()).getEnds(); // 获得过滤器的扩展名
            File newFile = null;
            if (file.getAbsolutePath().toUpperCase().endsWith(ends.toUpperCase())) { // 如果文件是以选定扩展名结束的，则使用原名
                newFile = file;
            } else {
                newFile = new File(file.getAbsolutePath() + "." + ends);// 否则加上选定的扩展名
            }
            try {
                newFile.getCanonicalPath();//获取产生异常证明不能保存
                imageCache = (BufferedImage) getScreenImage();
                ImageIO.write(imageCache, ends, newFile);
            } catch (IOException e) {
                System.err.println("save failed! ");
                saveImageFile();
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {//取消按钮
            jc.setVisible(false);
            return;
        } else if (result == JFileChooser.ERROR_OPTION) {//错误
            System.out.println("错误");
        }
        finishAndinitialization();//初始化并结束
    }


    /**
     * 注册截图事件监听器
     */
    public void addPrintScreenListener(PrintScreenListener listener) {
        this.listener = listener;
    }


    /**
     * 启动截图
     */
    public void start() {
        this.setAlwaysOnTop(true); //窗口最前面显示 
        this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));//设置鼠标为十字架
        this.setVisible(true);
    }


    /**
     * 初始化，并结束截图
     */
    public void finishAndinitialization() {
        this.dispose();//遗弃窗体
        imageCache = null;//清空缓存
        //重新构建选区和放大镜
        this.r = new Rect(this);
        this.sg = new ScreenGamer();
//		System.out.println("资源被回收");
    }
}
