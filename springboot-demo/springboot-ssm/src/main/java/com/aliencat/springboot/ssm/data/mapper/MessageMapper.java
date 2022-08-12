package com.aliencat.springboot.ssm.data.mapper;

import com.aliencat.springboot.ssm.common.entity.Result;
import com.aliencat.springboot.ssm.common.mapper.CommonMapper;
import com.aliencat.springboot.ssm.data.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author chengcheng
 * @Date 2022-07-15
 **/
@Mapper
public interface MessageMapper extends CommonMapper {


    @Select("select * from im_messages limit 10")
    Result<List<Message>> list();
}
