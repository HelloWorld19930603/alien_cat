package com.aliencat.testing.demo.whenthen;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

public class WhenThenTest {

    //模拟一个不存在的列表，但是返回的列表大小为100。
    //返回期望值
    @Test
    public void testSize() {
        Integer expected = 100;
        //T PowerMockito.mock(Class clazz); 生成可以用于模拟指定类的对象实例。
        List list = PowerMockito.mock(List.class);
        PowerMockito.when(list.size()).thenReturn(expected);
        Integer actual = list.size();
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    //返回期望异常
    @Test(expected = IndexOutOfBoundsException.class)
    public void testThrow() {
        int index = -1;
        Integer expected = 100;
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.when(mockList.get(index)).thenThrow(new IndexOutOfBoundsException());
        Integer actual = mockList.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    //返回期望应答
    @Test
    public void testAnswer() {
        int index = 1;
        Integer expected = 100;
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.when(mockList.get(index)).thenAnswer(invocation -> {
            Integer value = invocation.getArgument(0);
            return value * 100;
        });
        Integer actual = mockList.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }


    //调用真实方法
    @Test
    public void testCallRealMethod() {
        int expected = 1;
        List list = new ArrayList<>();
        list.add(new Object());
        List spylist = PowerMockito.spy(list);
        PowerMockito.when(spylist.size()).thenReturn(expected + 1);
        PowerMockito.when(spylist.size()).thenCallRealMethod();
        int actual = spylist.size();
        Assert.assertEquals("返回值不相等", expected, actual);
    }


}
