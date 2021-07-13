package com.aliencat.application.seckill.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 秒杀库存实体
 */
@Data
@ToString
public class Seckill {

    private long seckillId;

    private String name;

    private int number;

    private Date startTime;

    private Date endTime;

    private Date createTime;

}
