package com.aliencat.testing.mockdemo.staticmethod;


import com.aliencat.testing.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;



@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class})
public class StaticMethodTest {

    /**
     * mockStatic方法
     * 声明：
     * PowerMockito.mockStatic(Class clazz);
     * 用途：可以用于模拟类的静态方法，必须使用“@RunWith”和“@PrepareForTest”注解。
     */
    @Test
    public void testIsEmpty() {
        String string = "abc";
        boolean expected = true;
        PowerMockito.mockStatic(StringUtils.class);
        PowerMockito.when(StringUtils.isEmpty(string)).thenReturn(expected);
        boolean actual = StringUtils.isEmpty(string);
        Assert.assertEquals("返回值不相等", expected, actual);
        Assert.assertNotEquals("返回值相等", expected, StringUtils.isNotEmpty(string));
    }

    /**
     * spy语句:
     * 如果一个对象，我们只希望模拟它的部分方法，而希望其它方法跟原来一样，
     * 可以使用PowerMockito.spy方法代替PowerMockito.mock方法。
     * 于是，通过when语句设置过的方法，调用的是模拟方法；
     * 而没有通过when语句设置的方法，调用的是原有方法。
     * 声明：
     * PowerMockito.spy(Class clazz)；
     * 用途：用于模拟类的部分方法。
     */
    @Test
    public void testIsNotEmpty() {
        String string = null;
        boolean expected = true;
        PowerMockito.spy(StringUtils.class);
        PowerMockito.when(StringUtils.isEmpty(string)).thenReturn(!expected);
        boolean actual = StringUtils.isNotEmpty(string);
        Assert.assertEquals("返回值不相等", expected, actual);
    }
}
