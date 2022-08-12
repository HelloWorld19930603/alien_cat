package com.aliencat.springboot.ssm.data.service;

import com.aliencat.springboot.ssm.common.entity.Result;
import com.aliencat.springboot.ssm.data.entity.Message;
import com.aliencat.springboot.ssm.data.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author chengcheng
 * @Date 2022-07-15
 **/
@Service
public class MessageServiceImpl {

    @Autowired
    MessageMapper messageMapper;
    public Result<List<Message>> list() {

        return messageMapper.list();
    }
}
