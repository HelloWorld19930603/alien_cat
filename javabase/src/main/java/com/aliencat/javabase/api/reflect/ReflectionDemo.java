package com.aliencat.javabase.api.reflect;

import com.aliencat.javabase.api.annotation.MyAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*Class:代表一个字节码文件的对象，每当有类被加载进内存，JVM就会在堆上给
 *        该类创建一个代表该类的对象。每个类的Class对象是的。
 *Class类没有构造方法，获得类对应的Class方法有3种
 *1.：getClass()、2.类、接口.class 、3.Class.forName("类全名");
 *比较推荐使用第3种方式，使用前两种方式程序扩展性不好。
 *
 *Class类中定义了许多关于获取类中信息的方法：
 *1.获得该类的构造方法，属性，方法、实例的方法。包含特定情况的获得
 *2.获得该类的父类，实现的接口，该类的类加载器，类名、包名等。
 *3.判断该类的具体是接口、类、内部类等
 *4.方法中加Declared表示可以获得本类定义的任何方法和属性
 *
 *注意：关于获得到的方法、属性、构造器的具体操作被封装在import java.lang.reflect包里面
 *Method:里面最常用的方法invoke(对象，可变参数列表)--调用指定的方法
 *Field：get/set；获取和修改属性值
 *Constrcutor:使用newInstance(可变参数列表)--调用指定构造方法创建类的实例
 *注意：私有的要调用前先去掉访问权限限制setAccssible()
 * */
public class ReflectionDemo {

    public static void main(String[] args) throws Exception {

        //第一种方式获得Class对象，比较麻烦，要先创建对象，再使用对象调用方法
        MyObject myObject = new MyObject();
        Class clazz = myObject.getClass();

        //第二种方式获得Class对象。使用静态的属性创建
        Class clazz1 = MyObject.class;

        //使用Class对象的静态方法获得Class对象
        Class clazz2 = Class.forName("com.aliencat.javabase.api.reflect.MyObject");

        //获得该类的类加载器
        ClassLoader c = clazz2.getClassLoader();
        System.out.println(c.toString());

        Class clazz3 = String.class;
        System.out.println(clazz3.getClassLoader());

        //获得该类的实例
        Object obj = clazz2.newInstance();
        //获得该类的构造器---公开的，getDeclaredConstructors()--可以获得私有的
        Constructor[] con = clazz2.getDeclaredConstructors();
        for (Constructor cc : con) {
            System.out.print(cc + " ");
        }

        //获得类的方法
        Method[] mm = clazz2.getDeclaredMethods();
        for (Method mmm : mm) {
            System.out.print(mmm + " ");
        }

        System.out.println();
        //获取特定的方法
        Method m1 = clazz2.getMethod("walk", null);
        System.out.println(m1.toString());

        Field[] f = clazz2.getDeclaredFields();
        for (Field ff : f) {
            System.out.print(ff + " ");
        }
        System.out.println();
        //调用指定的方法-
        Method m2 = clazz2.getMethod("talk", int.class);
        m2.invoke(obj, 1);

        //在调用私有方法先设置访问权限
        Method m3 = clazz2.getDeclaredMethod("play", null);
        m3.setAccessible(true);
        m3.invoke(obj, null);


        //调用指定的构造方法创建类实例；先获取在调用
        Constructor cc = clazz2.getConstructor(int.class, String.class);
        Object o1 = cc.newInstance(12, "lisi");

        //获取和修改对象的属性值
        Field ffs = clazz2.getDeclaredField("age");
        ffs.setAccessible(true);
        ffs.set(obj, 30);
        Object oo = ffs.get(obj);
        System.out.println(oo);

    }

}

@MyAnnotation(key = "class")
class MyObject {

    @MyAnnotation(key = "field")
    public String name = "zhangsan";
    private int age;

    public MyObject() {
    }


    public MyObject(int age) {
        this.age = age;
    }

    public MyObject(int age, @MyAnnotation(value = "name") String name) {
        this.age = age;
        this.name = name;
        System.out.println("hello ");
    }

    @MyAnnotation(key = "method")
    private void play(@MyAnnotation("玩具") String toy) {
        System.out.println("--->play--->" + toy);
    }

    public void walk() {
        System.out.println("--->walk--->");
    }

    @Deprecated
    public void talk(int i) {
        System.out.println("talk---------->" + i);
    }
}
