package com.aliencat.springboot.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Author chengcheng
 * @Date 2022-07-20
 **/
public class MyService extends ServiceImpl {

    public void batchList(){
        this.saveBatch(null);
    }
}
