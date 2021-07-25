package com.aliencat.springboot.ssm.tbdescription.entity;

import com.aliencat.springboot.ssm.common.entity.PageCondition;
import lombok.Data;

/**
 * 用户描述表
 */
@Data
public class TbDescriptionVo extends PageCondition {
    private Integer id;
    private Integer userId;
    private String description;
}
