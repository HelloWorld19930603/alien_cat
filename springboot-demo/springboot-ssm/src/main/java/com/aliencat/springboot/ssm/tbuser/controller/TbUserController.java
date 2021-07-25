package com.aliencat.springboot.ssm.tbuser.controller;

import com.aliencat.springboot.ssm.common.controller.CommonController;
import com.aliencat.springboot.ssm.tbuser.entity.TbUser;
import com.aliencat.springboot.ssm.tbuser.entity.TbUserVo;
import com.aliencat.springboot.ssm.tbuser.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息表 前端控制器
 */
@RestController
@RequestMapping("/tbUser/")
public class TbUserController extends CommonController<TbUserVo, TbUser> {

    @Autowired
    private TbUserService tbUserService;

}