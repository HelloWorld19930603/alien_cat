package com.aliencat.springboot.ssm.tbuser.entity;

import com.aliencat.springboot.ssm.common.entity.PageCondition;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
public class TbUserVo extends PageCondition {
    private Integer id;
    private String username;
    private String password;
    private Date created;
    private Integer descriptionId;
}
