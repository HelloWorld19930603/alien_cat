package com.aliencat.springboot.mybatisplus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengcheng
 * @Date 2022-07-20
 **/
@RestController
public class MessageController {


    @RequestMapping("/query")
    public String query(){
        return "";
    }


    @RequestMapping("/insert")
    public String insert(){
        return "";
    }
}
