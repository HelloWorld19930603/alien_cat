package com.pattern.memento.demo01;

/**
 * 第二步
 * 创建备忘录管理者
 * 用户保存管理备忘录
 */
public class CareTaker {
    /**
     * 备忘录
     */
    private Memento memento;

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento = memento;
    }
}
