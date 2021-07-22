package com.pattern.singleton;

public class LockTest {
    /***
     * �˴�ʹ��volatile��Ϊ�˷�ֹʹ��ָ��������
     */
    public static volatile  LockTest lockTest;

    private LockTest(){

    }

    /**
     * ʹ��˫�ؼ������ֹ���߳�ͬʱ����
     * @return
     */
    public static LockTest getInstance(){
        if (lockTest==null){
            synchronized (LockTest.class){
                if (lockTest==null){
                    return new LockTest();
                }
            }
        }
       return lockTest;
    }

}
