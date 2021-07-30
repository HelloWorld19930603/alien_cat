package com.aliencat.springboot.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController("/redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate template;

    /**
     * 获取缓存
     */
    //测试：http://localhost:8080/redis/get/demo
    @RequestMapping("/get/{key}")
    public String get(@PathVariable("key") String key) {
        return template.opsForValue().get(key);
    }

    /**
     * 设置缓存
     */
    //测试：http://localhost:8080/redis/set/demo/val
    @RequestMapping("/set/{key}/{value}")
    public Boolean set(@PathVariable("key") String key, @PathVariable("value") String value) {
        boolean flag = true;
        try {
            template.opsForValue().set(key, value);
            //有效时长（秒）
            template.expire(key, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    /**
     * 发布消息
     */
    //测试：http://localhost:8080/redis/eventPush
    @RequestMapping("/eventPush")
    public Boolean eventPush() {
        template.convertAndSend("topic1", "topic1-第一种事件消息");
        template.convertAndSend("topic2", "topic2-第二种事件消息");
        return true;
    }
}
