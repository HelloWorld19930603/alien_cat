package com.pattern.memento.demo01;

/**
 * �ڶ���
 * ��������¼������
 * �û����������¼
 */
public class CareTaker {
    /**
     * ����¼
     */
    private Memento memento;

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento = memento;
    }
}
