package com.aliencat.application.seckill.dao;

import com.aliencat.application.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component(value = "successKilledMapper")
public interface SuccessKilledMapper {

    /**
     * 插入购买明细，可过滤重复
     *
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
