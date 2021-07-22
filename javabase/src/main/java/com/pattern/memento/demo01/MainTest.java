package com.pattern.memento.demo01;

/**
 * ����¼ģʽ
 * ����¼��Ҫ��������ɫ
 * 1.����¼��memento�� ���ڱ����ȡ����Ҫ����������
 * 2.�����ˣ�originator�� ���ڴ�������¼�ͻָ�����¼
 * 3.�����ߣ�caretaker��������¼
 */
public class MainTest {


    public static void main(String[] args) {
        MainTest mainTest = new MainTest();

        mainTest.showMemento();

    }

    /***
     * ����¼ģʽ����
     */
    public void showMemento() {
        //����һ�������߶��󲢸�ֵ��ʼֵ
        Originator originator = new Originator();
        originator.setId(1);
        originator.setVersion("version1");

        System.out.println("��ʼ��===>" + originator.toString());


        //�������߶�����б���
        Memento memento = originator.createMemento();


        //�ѱ�����Ϣ���������߹���
        CareTaker careTaker = new CareTaker();
        careTaker.setMemento(memento);

        //�Է�������Ϣ�����޸�
        originator.setVersion("version2");

        System.out.println("�޸ĺ�===>" + originator.toString());


        //���ݻָ�
        originator.recoverMemento(careTaker.getMemento());

        System.out.println("�ָ�����===>" + originator.toString());
    }
}
