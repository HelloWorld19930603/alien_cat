package com.aliencat.testing.mockdemo.privatefield;

import com.aliencat.testing.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

//私有属性测试
@RunWith(PowerMockRunner.class)
public class FieldTest {

    @InjectMocks
    private UserService UserService;


    /**
     * Whitebox.setInternalState方法
     *
     * 在用原生JUnit进行单元测试时，我们一般采用ReflectionTestUtils.setField方法设置私有属性值。
     * 使用PowerMock进行单元测试时，可以采用Whitebox.setInternalState方法设置私有属性值。
     */
    @Test
    public void testGetUserLimit() {
        Long expected = 1000L;
        Whitebox.setInternalState(UserService, "limit", expected);
        Long actual = UserService.getUserLimit();
        Assert.assertEquals("返回值不相等", expected, actual);
    }
}
