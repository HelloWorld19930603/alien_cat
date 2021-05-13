package com.aliencat.testing.utils;

import com.aliencat.testing.pojo.File;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;



@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class})
public class FileUtilsTest {

    /**
     * whenNew模拟构造方法
     * 声明：
     * PowerMockito.whenNew(MockClass.class).withNoArguments().thenReturn(expectedObject);
     * PowerMockito.whenNew(MockClass.class).withArguments(someArgs).thenReturn(expectedObject);
     * 用途：用于模拟构造方法。
     */
    @Test
    public void testWithArguments() throws Exception {
        String fileName = "test.txt";
        File file = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withArguments(fileName).thenReturn(file);
        PowerMockito.when(file.isFile()).thenReturn(true);
        Assert.assertTrue("返回值为假", FileUtils.isFile(fileName));
    }

    @Test
    public void testWithNoArguments() throws Exception {
        File file = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withNoArguments().thenReturn(file);
        PowerMockito.when(file.isClosed()).thenReturn(true);
        Assert.assertTrue("返回值为假", FileUtils.isClosed());
    }
}