package com.aliencat.springboot.mongodb.dao;


import com.aliencat.springboot.mongodb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    public User save(User user) {
        mongoTemplate.insert(user);
        return user;
    }
    public User getById(String id) {
        return mongoTemplate.findById(id, User.class);
    }
    public User deleteById(String id) {
        User user = mongoTemplate.findById(id, User.class);
        mongoTemplate.remove(user);
        return user;
    }

    public User update(User user) {
        mongoTemplate.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    public Map<String, Object> query(String userName,  int page,  int size) {

        // 条件
        Criteria criteria1 = Criteria.where("name").is(userName);
        Query query = new Query();
        if (userName!= null) {
            query.addCriteria(criteria1);
        }

        // 数量
        long total = mongoTemplate.count(query, User.class);

        // 分页
        query.skip((page - 1) * size).limit(size);

        List<User> data = mongoTemplate.find(query, User.class);
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("data", data);
        map.put("total", total);

        return map;
    }



}
