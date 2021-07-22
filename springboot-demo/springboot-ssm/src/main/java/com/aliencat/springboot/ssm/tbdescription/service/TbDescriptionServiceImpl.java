package com.aliencat.springboot.ssm.tbdescription.service;

import com.aliencat.springboot.ssm.common.service.CommonServiceImpl;
import com.aliencat.springboot.ssm.tbdescription.entity.TbDescription;
import com.aliencat.springboot.ssm.tbdescription.entity.TbDescriptionVo;
import com.aliencat.springboot.ssm.tbdescription.mapper.TbDescriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户描述表 服务实现类
 */
@Service
public class TbDescriptionServiceImpl extends CommonServiceImpl<TbDescriptionVo, TbDescription> implements TbDescriptionService {

    @Autowired
    private TbDescriptionMapper tbdescriptionMapper;
}
