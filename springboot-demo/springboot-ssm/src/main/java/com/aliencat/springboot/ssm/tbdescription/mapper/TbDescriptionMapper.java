package com.aliencat.springboot.ssm.tbdescription.mapper;

import com.aliencat.springboot.ssm.common.mapper.CommonMapper;
import com.aliencat.springboot.ssm.tbdescription.entity.TbDescription;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户描述表 Mapper 接口
 */
@Mapper
public interface TbDescriptionMapper extends CommonMapper<TbDescription> {

}

