package com.aliencat.application.webchat.service.impl;


import com.aliencat.application.webchat.mapper.UserMapper;
import com.aliencat.application.webchat.pojo.User;
import com.aliencat.application.webchat.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User selectUserByName(String name) {
        return userMapper.selectUserByName(name);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    @Override
    public User selectUserByNAP(String username, String password) throws Exception {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User user1 = userMapper.selectUserByNAP(user);
        return user1;
    }

    @Override
    public String signUp(User user) throws Exception {
        String success = userMapper.signUp(user).toString();
        return success;
    }
}
