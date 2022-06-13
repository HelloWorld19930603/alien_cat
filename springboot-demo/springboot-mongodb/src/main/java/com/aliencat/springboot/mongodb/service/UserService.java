package com.aliencat.springboot.mongodb.service;


import com.aliencat.springboot.mongodb.dao.UserDao;
import com.aliencat.springboot.mongodb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }

    public User getById(String id) {
        return userDao.getById(id);
    }

    public User deleteById(String id) {
        return userDao.deleteById(id);
    }

    public User update(User user) {
        return userDao.update(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public Map<String, Object> query(String userName, int page, int size) {
        return userDao.query(userName, page, size);
    }
}
