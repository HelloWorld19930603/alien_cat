package com.aliencat.testing.mockdemo.verify;

import com.aliencat.testing.dao.UserDAO;
import com.aliencat.testing.pojo.Circle;
import com.aliencat.testing.pojo.UserDO;
import com.aliencat.testing.utils.StringUtils;
import org.apache.catalina.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

/**
 * verify语句
 *
 * 验证是确认在模拟过程中，被测试方法是否已按预期方式与其任何依赖方法进行了交互。
 *
 * 格式：
 * Mockito.verify(mockObject[,times(int)]).someMethod(somgArgs);
 * 用途：用于模拟对象方法，直接返回期望的值、异常、应答，或调用真实的方法，无需执行原始方法。
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class})
public class VerifyTest {


    //1.验证调用方法
    @Test
    public void testMethod() {
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.doNothing().when(mockList).clear();
        mockList.clear();
        Mockito.verify(mockList).clear();
    }

    //2.验证调用次数
    //除times外，Mockito还支持atLeastOnce、atLeast、only、atMostOnce、atMost等次数验证器。
    @Test
    public void testTimes() {
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.doNothing().when(mockList).clear();
        mockList.clear();
        Mockito.verify(mockList, Mockito.times(1)).clear();
    }

    //3.验证调用顺序
    @Test
    public void testAdd() {
        List<Integer> mockedList = PowerMockito.mock(List.class);
        PowerMockito.doReturn(true).when(mockedList).add(Mockito.anyInt());
        mockedList.add(1);
        mockedList.add(2);
        mockedList.add(3);
        InOrder inOrder = Mockito.inOrder(mockedList);
        inOrder.verify(mockedList).add(1);
        inOrder.verify(mockedList).add(2);
        inOrder.verify(mockedList).add(3);
    }

    //4.验证调用参数
    @Test
    public void testArgumentCaptor() {
        Integer[] expecteds = new Integer[] {1, 2, 3};
        List<Integer> mockedList = PowerMockito.mock(List.class);
        PowerMockito.doReturn(true).when(mockedList).add(Mockito.anyInt());
        for (Integer expected : expecteds) {
            mockedList.add(expected);
        }
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockedList, Mockito.times(3)).add(argumentCaptor.capture());
        Integer[] actuals = argumentCaptor.getAllValues().toArray(new Integer[0]);
        Assert.assertArrayEquals("返回值不相等", expecteds, actuals);
    }

    /**
     * 5.确保验证完毕
     *
     * Mockito提供Mockito.verifyNoMoreInteractions方法，在所有验证方法之后可以使用此方法，以确保所有调用都得到验证。
     * 如果模拟对象上存在任何未验证的调用，将会抛出NoInteractionsWanted异常。
     */
    @Test
    public void testVerifyNoMoreInteractions() {
        List<Integer> mockedList = PowerMockito.mock(List.class);
        Mockito.verifyNoMoreInteractions(mockedList); // 执行正常
        mockedList.isEmpty();
        Mockito.verify(mockedList).isEmpty();   //若不进行此行验证，则下面的verifyNoMoreInteractions抛出异常
        Mockito.verifyNoMoreInteractions(mockedList);

    }

    /**
     * 6.验证静态方法
     *
     * Mockito没有静态方法的验证方法，但是PowerMock提供这方面的支持。
     */
    @Test
    public void testVerifyStatic() {
        PowerMockito.mockStatic(StringUtils.class);
        String expected = "abc";
        StringUtils.isEmpty(expected);
        PowerMockito.verifyStatic(StringUtils.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        StringUtils.isEmpty(argumentCaptor.capture());
        Assert.assertEquals("参数不相等", argumentCaptor.getValue(), expected);
    }
}
