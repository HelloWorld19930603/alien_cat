package com.aliencat.springboot.ssm.tbuser.service;

import com.aliencat.springboot.ssm.common.service.CommonServiceImpl;
import com.aliencat.springboot.ssm.tbuser.entity.TbUser;
import com.aliencat.springboot.ssm.tbuser.entity.TbUserVo;
import com.aliencat.springboot.ssm.tbuser.mapper.TbUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息表 服务实现类
 */
@Service
public class TbUserServiceImpl extends CommonServiceImpl<TbUserVo, TbUser> implements TbUserService {

    @Autowired
    private TbUserMapper tbuserMapper;
}
