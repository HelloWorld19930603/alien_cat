package com.pattern.memento.demo01;

/**
 * 备忘录模式
 * 备忘录主要有三个角色
 * 1.备忘录（memento） 用于保存和取出需要备忘的内容
 * 2.发起人（originator） 用于创建备忘录和恢复备忘录
 * 3.管理者（caretaker）管理备忘录
 */
public class MainTest {


    public static void main(String[] args) {
        MainTest mainTest = new MainTest();

        mainTest.showMemento();

    }

    /***
     * 备忘录模式测试
     */
    public void showMemento() {
        //创建一个发起者对象并赋值初始值
        Originator originator = new Originator();
        originator.setId(1);
        originator.setVersion("version1");

        System.out.println("初始化===>" + originator.toString());


        //将发起者对象进行备份
        Memento memento = originator.createMemento();


        //把备份信息交给管理者管理
        CareTaker careTaker = new CareTaker();
        careTaker.setMemento(memento);

        //对发起者信息进行修改
        originator.setVersion("version2");

        System.out.println("修改后===>" + originator.toString());


        //备份恢复
        originator.recoverMemento(careTaker.getMemento());

        System.out.println("恢复备份===>" + originator.toString());
    }
}
