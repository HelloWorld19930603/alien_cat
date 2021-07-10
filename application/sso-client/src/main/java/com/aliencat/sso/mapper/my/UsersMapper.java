package com.aliencat.sso.mapper.my;


import com.aliencat.sso.mapper.MyMapper;
import com.aliencat.sso.pojo.Users;
import org.springframework.stereotype.Component;

@Component(value = "userMapper")
public interface UsersMapper extends MyMapper<Users> {
}