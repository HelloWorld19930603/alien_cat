package com.aliencat.javabase.jvm;

/**
 * finalize()是Object中的方法，当垃圾回收器将要回收对象所占内存之前被调用，
 * 即当一个对象被虚拟机宣告死亡时会先调用它finalize()方法，
 * finalize()只会在对象内存回收前被调用一次,
 * finalize()的调用具有不确定行，只保证方法会调用，但不保证方法里的任务会被执行完。
 */
public class FinalizeTest {

    public FinalizeTest finalizeTest;
    private String name;

    public FinalizeTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws InterruptedException {
        new FinalizeTest("匿名实例");
        FinalizeTest f1 = new FinalizeTest("Test1");
        FinalizeTest f2 = new FinalizeTest("Test2");
        FinalizeTest f3 = new FinalizeTest("Test3");
        FinalizeTest f4 = new FinalizeTest("Test4");
        f1.finalizeTest = f3;
        f2.finalizeTest = f4;
        f2 = null;
        f3 = null;
        f4 = null;

        System.gc();
        Thread.sleep(3000);

    }

    protected void finalize() throws Throwable {
        System.out.println("finalize:" + name);
        /**
         * 执行父类的垃圾清理机制
         * 垃圾回收器清理对象时的清理机制，检查该对象是否可以清理
         * finalize()是垃圾回收前首先调用的方法
         */
        super.finalize();


    }
}
