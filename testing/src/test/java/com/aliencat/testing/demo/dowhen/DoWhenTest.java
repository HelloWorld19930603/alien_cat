package com.aliencat.testing.demo.dowhen;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

/**
 * doReturn().when()模式
 * 声明：
 * PowerMockito.doReturn(expectedValue).when(mockObject).someMethod(someArgs);
 * PowerMockito.doThrow(expectedThrowable).when(mockObject).someMethod(someArgs);
 * PowerMockito.doAnswer(expectedAnswer).when(mockObject).someMethod(someArgs);
 * PowerMockito.doNothing().when(mockObject).someMethod(someArgs);
 * PowerMockito.doCallRealMethod().when(mockObject).someMethod(someArgs);
 *
 * 用途：用于模拟对象方法，直接返回期望的值、异常、应答，或调用真实的方法，无需执行原始方法。
 */
public class DoWhenTest {


    @Test
    public void testDoReturn() {
        int index = 0;
        Integer expected = 100;
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.doReturn(expected).when(mockList).get(index);
        Integer actual = mockList.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    //返回期望异常
    @Test(expected = IndexOutOfBoundsException.class)
    public void testDoThrow() {
        int index = -1;
        Integer expected = 1;
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.doThrow(new IndexOutOfBoundsException()).when(mockList).get(index);
        Integer actual = mockList.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }


    //返回期望应答
    @Test
    public void testDoAnswer() {
        int index = 1;
        Integer expected = 100;
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.doAnswer(invocation -> {
            Integer value = invocation.getArgument(0);
            return value * 100;
        }).when(mockList).get(index);
        Integer actual = mockList.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    //调用真实方法
    @Test
    public void testDoCallRealMethod() {
        int index = 0;
        Integer expected = 100;
        List<Integer> oldList = new ArrayList<>();
        oldList.add(expected);
        List<Integer> spylist = PowerMockito.spy(oldList);
        PowerMockito.doCallRealMethod().when(spylist).get(index);
        Integer actual = spylist.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }
}
