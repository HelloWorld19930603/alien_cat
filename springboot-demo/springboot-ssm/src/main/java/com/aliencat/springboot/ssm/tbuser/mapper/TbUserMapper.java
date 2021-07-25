package com.aliencat.springboot.ssm.tbuser.mapper;

import com.aliencat.springboot.ssm.common.mapper.CommonMapper;
import com.aliencat.springboot.ssm.tbuser.entity.TbUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表 Mapper 接口
 */
@Mapper
public interface TbUserMapper extends CommonMapper<TbUser> {

}

