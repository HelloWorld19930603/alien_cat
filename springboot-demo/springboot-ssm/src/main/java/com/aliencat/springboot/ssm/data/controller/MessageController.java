package com.aliencat.springboot.ssm.data.controller;

import com.aliencat.springboot.ssm.common.entity.Result;
import com.aliencat.springboot.ssm.data.entity.Message;
import com.aliencat.springboot.ssm.data.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author chengcheng
 * @Date 2022-07-15
 **/
@Controller
public class MessageController {

    @Autowired
    MessageServiceImpl messageService;

    @GetMapping("list")
    public Result<List<Message>> list() {
        return messageService.list();
    }


}
