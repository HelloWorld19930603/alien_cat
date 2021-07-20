package com.pattern.memento.demo01;

/**
 * 第一步
 * 创建备忘录
 * 保存一个状态字符串或者状态类
 */
public class Memento {
    /**
     * 备份熟悉 版本
     */
    private String version;

    public Memento(String version) {
        this.version=version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
