package com.aliencat.application.seckill.dao;

import com.aliencat.application.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 秒杀库存DAO接口
 */
@Component(value = "seckillMapper")
public interface SeckillMapper<T> extends Mapper<T>, MySqlMapper<T> {

    /**
     * 减库存
     *
     * @param seckillId
     * @param killTime
     * @return 如果影响行数等于>1，表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     *
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     *
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);

}
