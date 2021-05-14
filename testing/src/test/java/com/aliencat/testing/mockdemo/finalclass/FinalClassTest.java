package com.aliencat.testing.mockdemo.finalclass;

import com.aliencat.testing.pojo.Circle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 当模拟非final类（接口、普通类、虚基类）的非final方法时，不必使用@RunWith和@PrepareForTest注解。
 * 当模拟final类或final方法时，必须使用@RunWith和@PrepareForTest注解。
 * 注解形如：
 * @RunWith(PowerMockRunner.class)
 * @PrepareForTest({TargetClass.class})
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Circle.class})
public class FinalClassTest {
    @Test
    public void testGetArea() {
        double expectArea = 3.14D;
        Circle circle = PowerMockito.mock(Circle.class);
        PowerMockito.when(circle.getArea()).thenReturn(expectArea);
        double actualArea = circle.getArea();
        Assert.assertEquals("返回值不相等", expectArea, actualArea, 1E-6D);
    }
}
