package com.aliencat.testing.mockdemo.privatemethod;

import com.aliencat.testing.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

//私有方法测试
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService.class})
public class MethodTest {

    //1.通过when实现
    @Test
    public void testWhen() throws Exception {
        Long userId = 1L;
        boolean expected = false;
        UserService userService = PowerMockito.spy(new UserService());
        PowerMockito.when(userService, "isSuperUser", userId).thenReturn(!expected);
        boolean actual = userService.isNotSuperUser(userId);
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    /**
     * 2.通过stub实现
     *
     * 通过模拟方法stub(存根)，也可以实现模拟私有方法。但是，只能模拟整个方法的返回值，而不能模拟指定参数的返回值。
     */
    @Test
    public void testStub() throws Exception {
        Long userId = 1L;
        boolean expected = false;
        UserService userService = PowerMockito.spy(new UserService());
        PowerMockito.stub(PowerMockito.method(UserService.class, "isSuperUser", Long.class)).toReturn(!expected);
        boolean actual = userService.isNotSuperUser(userId);
        Assert.assertEquals("返回值不相等", expected, actual);
    }

    // 3.测试私有方法
    @Test
    public void testMethod() throws Exception {
        Long userId = 1L;
        boolean expected = false;
        UserService userService = new UserService();
        Method method = PowerMockito.method(UserService.class, "isSuperUser", Long.class);
        Object actual = method.invoke(userService, userId);
        Assert.assertEquals("返回值不相等", expected, actual);
    }


    //4.验证私有方法
    @Test
    public void testVerifyPrivate() throws Exception {
        Long userId = 1L;
        boolean expected = false;
        UserService userService = PowerMockito.spy(new UserService());
        PowerMockito.when(userService, "isSuperUser", userId).thenReturn(!expected);
        boolean actual = userService.isNotSuperUser(userId);
        PowerMockito.verifyPrivate(userService).invoke("isSuperUser", userId);
        Assert.assertEquals("返回值不相等", expected, actual);
    }
}
