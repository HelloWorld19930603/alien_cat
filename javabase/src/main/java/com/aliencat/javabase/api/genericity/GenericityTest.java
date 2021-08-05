package com.aliencat.javabase.api.genericity;

import java.util.ArrayList;
import java.util.List;

/**
 * java泛型特性的测试
 * <p>
 * 结论：
 * 协变：extends/向上转换/不能add/只能get（T及父类）
 * 逆变：super/向下转换/不能get/只能add（T及子类）
 */
public class GenericityTest {

    public static void main(String[] args) {
        List<Parent> list1 = new ArrayList<Parent>();
        list1.add(new Parent());
        list1.add(new Child());

        //下面这行代码会报错
        //因为泛型没有内建的协变类型，无法将List<Parent>和ArrayList<Child>关联起来，所以在编译阶段就会出现错误。
        //List<Parent> list2 = new ArrayList<Child>();

        //泛型的协变
        //我们可以利用通配符实现泛型的协变：<? extends T>子类通配符；这个通配符定义了?继承自T，可以帮助我们实现向上转换
        List<? extends Parent> list3 = new ArrayList<Child>();
        //list3 = new ArrayList<Object>();  //由于Object不是Parent子类，这里会报错
        /**
         * 由于不能确定list的类型是Fruit的子类当中具体的哪一个？（有多个类都继承自Fruit），
         * 这也就导致了一旦使用了<? extends T>向上转换之后，不能再向list中添加任何类型的对象了，
         * 这个时候只能选择从list当中get数据而不能add。
         */
        //list3.add(new Child());  //不允许添加数据，编译报错，但是我们可以通过强转来使用
        list3 = list1;
        //由于父类只能由一个，编译器能确定get的数据必然是Parent的子类，所以可以获取成功
        Parent parent = list3.get(0);

        //泛型的逆变则和协变相反，它是向下转换
        List<? super Child> list4 = new ArrayList<Parent>();
        /**
         * 逆变使用通配符<? super T>（超类通配符），如上面代码，
         * Parent是Child的超类，则这个时候对于JVM来说，
         * 它能确定list的类型的超类肯定是Child或者Child的父类，
         * 换言之该类型就是Child或者Child的子类，所以和上面的协变一样，
         * 既然确定了类型的范围，那么list能够保证add的类型就是Child或者Child的子类了。
         */
        list4.add(new Child());
        //list4.add(new Parent());  //编译报错
        //由于子类存在多种情况，所以编译器不能确定获取到的是哪个子类
        //Child child = list4.get(0); //编译报错

        //通过强转来使用
        list3 = (List<? extends Parent>) list4;
        parent = list3.get(0);

        /**
         * PECS（producer-extends, consumer-super）
         * 由于以上特性，可以作以下用途：
         * 协变：如果参数化类型表示一个生产者，就使用<? extends T>。比如list.get(0)这种，list作为数据源producer；
         * 逆变：如果它表示一个消费者，就使用<? super T>。比如：list.add(new Apple())，list作为数据处理端consumer。
         */
    }

    /**
     * Java的泛型本质上不是真正的泛型，而是利用了类型擦除，比如下面的代码就会出现错误：
     * 'method(List<String>)' clashes with 'method(List<Integer>)'; both methods have same erasure
     * 原因是Java在编译的时候会把泛型<String>和<Integer>都给擦除掉。
     * Java的泛型机制是在编译级别实现的。
     * 编译器生成的字节码在运行期间并不包含泛型的类型信息。
     * PS：其实并没有真正的被擦除，javap -l -p -v -c可以看到LocalVariableTypeTable里面有方法参数类型的签名。
     */
    /*
    public void method(List<String> list) {
    }
    */
    public void method(List<Integer> list) {
    }

}


class Parent {
}


class Child extends Parent {
}