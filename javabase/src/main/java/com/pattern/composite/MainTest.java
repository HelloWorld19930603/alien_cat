package com.pattern.composite;

/**
 * 组合模式
 *  desc:组合模式使对象的部分和整体呈现出一种树状的层次感，并且使得用户对
 *  单个对象和组合对象的操作具有一致性
 *
 *  业务需求：
 *      1.所有相同的组件组成了大的系统
 *      2.每个系统都具有相同的接口和结构
 *      3.也可以有不同的功能来处理
 */
public class MainTest {
    public static void main(String[] args) {
        Composite root = new Composite("root");
        Composite one1 = new Composite("一级公司1");
        Composite one2 = new Composite("一级公司2");
        Composite two1 = new Composite("二级公司1");
        Composite two2 = new Composite("二级公司2");
        Composite three1 = new Composite("三级公司1");
        Composite three2 = new Composite("三级公司2");

        root.add(one1);
        root.add(one2);
        one1.add(two1);
        one2.add(two2);
        two1.add(three1);
        two1.add(three2);
        root.display(0);
    }
}
