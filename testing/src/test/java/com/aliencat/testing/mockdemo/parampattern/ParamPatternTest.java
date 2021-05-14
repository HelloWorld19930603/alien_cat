package com.aliencat.testing.mockdemo.parampattern;

import com.aliencat.testing.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

/**
 * 参数匹配器
 *
 * 在执行单元测试时，有时候并不关心传入的参数的值，可以使用参数匹配器。
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class})
public class ParamPatternTest {

    /**
     * 1  参数匹配器(any)
     *
     * Mockito提供Mockito.anyInt()、Mockito.anyString、Mockito.any(Class<T> clazz)等来表示任意值。
     */
    @Test
    public void testAny() {
        int index = 1;
        Integer expected = 100;
        List<Integer> mockList = PowerMockito. mock (List.class);
        PowerMockito.when (mockList.get(Mockito.anyInt())).thenReturn(expected);
        Integer actual = mockList . get (index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    /**
     * 2  参数匹配器(eq)
     *
     * 当我们使用参数匹配器时，所有参数都应使用匹配器。
     * 如果要为某一参数指定特定值时，就需要使用Mockito.eq()方法。
     */
    @Test
    public void testEq() {
        String string = "abc";
        String prefix = "b";
        boolean expected = true;
        PowerMockito.spy(StringUtils.class);
        PowerMockito.when(StringUtils.startsWith(Mockito.anyString(), Mockito.eq(prefix))).thenReturn(expected);
        boolean actual = StringUtils.startsWith(string, prefix);
        Assert.assertEquals("返回值不相等", expected, actual);
    }


    /**
     * 3  附加匹配器
     *
     * Mockito的AdditionalMatchers类提供了一些很少使用的参数匹配器，
     * 我们可以进行参数大于(gt)、小于(lt)、大于等于(geq)、小于等于(leq)等比较操作，
     * 也可以进行参数与(and)、或(or)、非(not)等逻辑计算等。
     */
    @Test
    public void testLt() {
        int index = 1;
        Integer expected = 100;
        List<Integer> mockList = PowerMockito.mock(List.class);
        PowerMockito.when(mockList.get(AdditionalMatchers.geq(0))).thenReturn(expected);
        PowerMockito.when(mockList.get(AdditionalMatchers.lt(0))).thenThrow(new IndexOutOfBoundsException());
        Integer actual = mockList.get(index);
        Assert.assertEquals("返回值不相等", expected, actual);
    }



}
