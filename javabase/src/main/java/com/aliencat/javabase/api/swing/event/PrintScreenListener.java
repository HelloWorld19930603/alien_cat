package com.aliencat.javabase.api.swing.event;


/**
 * <p>嵌入PrintScreen的项目必须实现此接口才能产生回调机制获取截区图片</p>
 */
public interface PrintScreenListener {

    /**
     * 获取图片后的操作方法
     */
    public void PrintScreenSaved(PrintScreenEvent e);

}
