package com.aliencat.application.webchat.mapper;

import com.aliencat.application.webchat.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User selectUserByName(String username);

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    Integer signUp(User user);

    /**
     * 获取用户信息
     */
    User selectUserByNAP(User user);
}
