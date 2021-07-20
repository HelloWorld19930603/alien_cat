package com.aliencat.javabase.api.list;

import org.junit.Test;

import java.util.*;

public class ArrayListTest {

    @Test
    public void test1() {
        //创建集合对象
        List list = new ArrayList();
        //list = Collections.emptyList();
        list = new ArrayList(Arrays.asList("1", "2", "3"));

        //1、添加对象
        list.add("hello,world!");
        list.add(456);
        list.add(new Object());
        list.add(2, "java"); //指定位置上添加

        //2、集合中元素个数： size();
        System.out.println(list.size());

        //3、集合是否为空： isEmpty(); 返回Boolean类型
        System.out.println("集合是否为空：" + list.isEmpty());

        //4、指定集合中对象添加到此集合中： addAll(Collection c);
        List list2 = new ArrayList();
        list2.add("hello");
        list2.add(123);
        list.addAll(list2);
        System.out.println(list);

        //5、删除： remove(); --根据对象、索引删除
        list.remove("hello");//--根据对象删除
        list.remove(new Integer(123));//--需进行类型转换
        list.remove(0); //--根据索引删除
        System.out.println(list);

        //list.removeAll(list2); --删除list中所有list2的元素
        //list.retainAll(list2); --留下交集

        //6、判断对象是否存在 ： contains(Object obj); --返回Boolean类型
        System.out.println(list.contains("hello"));

        //7、判断集合是否被包含： containsAll(Collection c);
        System.out.println(list.containsAll(list2));
        //8、清空所有对象clear();
        list.clear();

        //9、获取指定索引位置上的元素对象：get(int index);
        System.out.println(list2.get(0));

        //10、设置 ：set(int index,Object element)
        list2.set(1, "hello");

        //11、查找元素在集合中的位置 ：indexOf(Object obj);--找不到返回-1
        System.out.println(list2.indexOf("hello"));

        //12、遍历集合中的元素
        for (Object obj : list) {
            System.out.println(obj);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        //使用迭代器*
        Iterator ite = list.iterator();//返回Iterator类型的迭代器对象,正向遍历
        while (ite.hasNext()) { //hasNext();判断集合中是否有元素对象，返回Boolean类型
            Object obj = ite.next(); //next();--返回从基础集合中移除的最后的元素
            System.out.println(obj);
        }
        ListIterator listIte = list2.listIterator();
        while (listIte.hasNext()) { //next:后
            System.out.println(listIte.next());
        }
        //--逆向遍历
        while (listIte.hasPrevious()) { //previous:前
            System.out.println(listIte.previous());
        }
    }


}
