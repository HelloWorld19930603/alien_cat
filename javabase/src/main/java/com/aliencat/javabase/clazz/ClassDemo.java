package com.aliencat.javabase.clazz;

/**
 * 1.加载顺序：静态>普通>main方法>构造
 * 2.排在前面的同等属性或方法先加载
 * 3.属性优于代码块先加载
 * 4.内部类默认不会被加载
 * <p>
 * 注意：
 * 若是在用成员变量初始化当前class对象，则会报java.lang.StackOverflowError异常。
 */
public class ClassDemo implements Cloneable {

    static int i;
    private static ClassDemo demo = new ClassDemo();
    private static Class classDemo = ClassDemo.class;
    //private  Class classDemo = ClassDemo.class;  //此行代码会抛出异常
    private static String staticStr = printStatic(3);

    static {
        i++;
        System.out.println("静态代码块");
    }

    private String str = print();

    {
        i++;
        System.out.println("普通代码块");
    }

    ClassDemo() {
        i++;
        System.out.println("构造器");
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        ClassDemo classDemo = new ClassDemo();
        Class clazz = classDemo.getClass();
        ClassDemo classDemoClone = (ClassDemo) classDemo.clone();
        System.out.println(classDemo == classDemoClone);
        System.out.println(classDemo.i);
    }

    public static String printStatic(int i) {
        System.out.println("我是第" + i + "个static成员变量");
        return "";
    }

    public String print() {
        System.out.println("我是成员变量");
        return "";
    }

    public static class InnerClass {

        private static int i2 = printStatic();
        public int i1 = print();

        public static int printStatic() {
            System.out.println("我是内部中的静态成员变量");
            return 0;
        }

        public int print() {
            System.out.println("我是内部类中的成员变量");
            return 0;
        }
    }

}
