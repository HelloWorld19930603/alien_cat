package com.aliencat.application.seckill.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 成功秒杀实体
 */
@Data
@ToString
public class SuccessKilled {

    private long seckillId;

    private long userPhone;

    private short state;

    private Date creteTime;

    // 多对一的复合属性
    private Seckill seckill;
}
