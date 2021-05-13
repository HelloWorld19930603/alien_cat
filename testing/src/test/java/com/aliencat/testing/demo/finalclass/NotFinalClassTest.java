package com.aliencat.testing.demo.finalclass;

import com.aliencat.testing.pojo.Rectangle;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

//模拟非final类普通方法
public class NotFinalClassTest {
    @Test
    public void testGetArea() {
        double expectArea = 100.0D;
        Rectangle rectangle = PowerMockito.mock(Rectangle.class);
        PowerMockito.when(rectangle.getArea()).thenReturn(expectArea);
        double actualArea = rectangle.getArea();
        Assert.assertEquals("返回值不相等", expectArea, actualArea, 1E-6D);
    }
}