package com.pattern.memento.demo01;

/**
 * ��һ��
 * ��������¼
 * ����һ��״̬�ַ�������״̬��
 */
public class Memento {
    /**
     * ������Ϥ �汾
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
