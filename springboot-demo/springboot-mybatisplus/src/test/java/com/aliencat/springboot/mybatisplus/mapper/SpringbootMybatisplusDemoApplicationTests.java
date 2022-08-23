package com.aliencat.springboot.mybatisplus.mapper;

import com.aliencat.springboot.mybatisplus.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author chengcheng
 * @Date 2022-07-20
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootMybatisplusDemoApplicationTests {

    //继承了BaseMapper，所有方法都来自父类
    //当然我们也可以自己写扩展的方法

    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        // 参数是一个 Wrapper ，条件构造器，这里我们先不用 null
        //  测试查询全部用户
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }


    @Test
    public void batchInsert() {
        List<User> users = userMapper.selectList(null);
        userMapper.insertListWithIgnore(users);
        users.forEach(System.out::println);
    }
}

