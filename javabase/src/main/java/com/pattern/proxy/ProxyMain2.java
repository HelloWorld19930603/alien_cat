package com.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//С���ӿ�
interface Children {
    void eatChicken();

    void eatFruits();

    void cry();
}

public class ProxyMain2 {

    public static void main(String[] args) {
        Children baby = new Baby();
        Dad dad = new Dad(baby);
        //ʹ��java�Ķ�̬����ʽ��������
        Children $dad = (Children) Proxy.newProxyInstance(baby.getClass().getClassLoader(), baby.getClass().getInterfaces(), dad);
        $dad.eatChicken();
        $dad.eatFruits();
        $dad.cry();
    }
}

//����Ҫ�к��ӵİְ֣���Ϊ��Ҫ������
class Dad implements InvocationHandler {
    private Children children;

    public Dad(Children children) {
        this.children = children;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //�жϺ����Ƿ�Ҫ�Զ���
        if (method.getName().indexOf("eat") != -1) {
            wash();
            method.invoke(children, args);//ʹ�÷���ķ�ʽִ�з���
        }

        return null;
    }

    private void wash() {
        System.out.println("ϴ��");

    }
}

class Baby implements Children {
    @Override
    public void eatChicken() {
        System.out.println("�Լ���");
    }

    @Override
    public void eatFruits() {
        System.out.println("��ˮ��");
    }

    @Override
    public void cry() {
        System.out.println("��");
    }
}