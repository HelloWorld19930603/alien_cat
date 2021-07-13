package com.aliencat.application.webchat.service.interfaces;


import com.aliencat.application.webchat.pojo.User;

public interface UserService {
    User selectUserByName(String name);

    /**
     * 注册用户
     *
     * @param user
     * @return
     * @throws Exception
     */
    String signUp(User user) throws Exception;

    /**
     * 登录
     */
    User selectUserByNAP(String username, String password) throws Exception;
}
