package com.pattern.composite;


/**
 * ���������Ҫ�̳������Ľӿ�
 */
public abstract class Component {
    private String name;

    public Component(String name){
        this.name=name;
    };

    //�������
    public void add(Component component){};

    //�Ƴ����
    public void remove(Component component){};

    //��ӡ��ǰ�������
    public void display(int deepth){
        String str = "";
        for (int i = 0; i < deepth; i++) {
            str+="--";
        }
        System.out.println(str+name);
    };

    //��ǰ�����ְ��
    public void duty(){};

}
