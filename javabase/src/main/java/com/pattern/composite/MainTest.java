package com.pattern.composite;

/**
 * ���ģʽ
 *  desc:���ģʽʹ����Ĳ��ֺ�������ֳ�һ����״�Ĳ�θУ�����ʹ���û���
 *  �����������϶���Ĳ�������һ����
 *
 *  ҵ������
 *      1.������ͬ���������˴��ϵͳ
 *      2.ÿ��ϵͳ��������ͬ�Ľӿںͽṹ
 *      3.Ҳ�����в�ͬ�Ĺ���������
 */
public class MainTest {
    public static void main(String[] args) {
        Composite root = new Composite("root");
        Composite one1 = new Composite("һ����˾1");
        Composite one2 = new Composite("һ����˾2");
        Composite two1 = new Composite("������˾1");
        Composite two2 = new Composite("������˾2");
        Composite three1 = new Composite("������˾1");
        Composite three2 = new Composite("������˾2");

        root.add(one1);
        root.add(one2);
        one1.add(two1);
        one2.add(two2);
        two1.add(three1);
        two1.add(three2);
        root.display(0);
    }
}
