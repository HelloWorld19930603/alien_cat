package com.pattern.composite;


/**
 * 所有组件需要继承声明的接口
 */
public abstract class Component {
    private String name;

    public Component(String name){
        this.name=name;
    };

    //增加组件
    public void add(Component component){};

    //移除组件
    public void remove(Component component){};

    //打印当前组件内容
    public void display(int deepth){
        String str = "";
        for (int i = 0; i < deepth; i++) {
            str+="--";
        }
        System.out.println(str+name);
    };

    //当前组件的职责
    public void duty(){};

}
