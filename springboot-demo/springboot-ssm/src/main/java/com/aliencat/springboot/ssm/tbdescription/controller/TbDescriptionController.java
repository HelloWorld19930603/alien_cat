package com.aliencat.springboot.ssm.tbdescription.controller;

import com.aliencat.springboot.ssm.common.controller.CommonController;
import com.aliencat.springboot.ssm.tbdescription.entity.TbDescription;
import com.aliencat.springboot.ssm.tbdescription.entity.TbDescriptionVo;
import com.aliencat.springboot.ssm.tbdescription.service.TbDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户描述表 前端控制器
 */
@RestController
@RequestMapping("/tbDescription/")
public class TbDescriptionController extends CommonController<TbDescriptionVo, TbDescription> {

    @Autowired
    private TbDescriptionService tbDescriptionService;

}