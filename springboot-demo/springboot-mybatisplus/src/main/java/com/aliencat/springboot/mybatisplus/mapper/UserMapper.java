package com.aliencat.springboot.mybatisplus.mapper;

import com.aliencat.springboot.mybatisplus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Author chengcheng
 * @Date 2022-07-20
 **/
  //代表持久层
public interface UserMapper extends BaseMapper<User> {
    void insertListWithIgnore(List<User> users);
}