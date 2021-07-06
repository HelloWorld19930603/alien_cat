package com.aliencat.mapper;


import com.aliencat.mapper.my.MyMapper;
import com.aliencat.pojo.Users;
import org.springframework.stereotype.Component;

@Component(value = "userMapper")
public interface UsersMapper extends MyMapper<Users> {
}