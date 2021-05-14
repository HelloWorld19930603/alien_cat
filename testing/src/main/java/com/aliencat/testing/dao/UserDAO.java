package com.aliencat.testing.dao;

import com.aliencat.testing.pojo.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    public Long getIdByName(String name) {

        return null;
    }

    public void create(UserDO create) {
    }

    public void modify(UserDO modify) {
        modify.setName("李四");
    }
}
