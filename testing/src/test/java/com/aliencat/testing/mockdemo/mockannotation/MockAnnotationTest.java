package com.aliencat.testing.mockdemo.mockannotation;

import com.aliencat.testing.dao.UserDAO;
import com.aliencat.testing.pojo.UserDO;
import com.aliencat.testing.pojo.vo.UserVO;
import com.aliencat.testing.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.modules.junit4.PowerMockRunner;

//@RunWith注解 指定JUnit 使用 PowerMock 框架中的单元测试运行器。
@RunWith(PowerMockRunner.class)
//@PrepareForTest注解
//@PrepareForTest({ UserService.class })
public class MockAnnotationTest {

    //@Mock注解创建了一个全部Mock的实例，所有属性和方法全被置空（0或者null）。
    //@Spy注解创建了一个没有Mock的实例，所有成员方法都会按照原方法的逻辑执行，直到被Mock返回某个具体的值为止。需要配合@RunWith注解使用。
    @Mock
    private UserDAO userDAO;
    //@InjectMocks注解创建一个实例，这个实例可以调用真实代码的方法，其余用@Mock或@Spy注解创建的实例将被注入到用该实例中。
    @InjectMocks
    private UserService userService;
    //@Captor注解在字段级别创建参数捕获器。但是，在测试方法启动前，必须调用MockitoAnnotations.openMocks(this)进行初始化。
    @Captor
    private ArgumentCaptor<UserDO> argumentCaptor;
    @Before
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateUser() {
        UserVO userVO = new UserVO();
        userVO.setName("张三");
        userService.modifyUser(userVO);
        Mockito.verify(userDAO).modify(argumentCaptor.capture());
        UserDO userDO = argumentCaptor.getValue();
        Assert.assertNotNull("用户实例为空", userDO);
        Assert.assertEquals("用户名称不相等", userVO.getName(), userDO.getName());
    }
}