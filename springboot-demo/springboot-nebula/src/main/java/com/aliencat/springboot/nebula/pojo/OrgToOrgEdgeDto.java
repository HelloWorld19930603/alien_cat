package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 组织与组织之间的关系
 */
@Data
public class OrgToOrgEdgeDto {

    private String fromId;

    private String toId;

    /**
     * 遍历
     */
    private String ergodic;

    /**
     * 来源
     */
    private String source;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 组织信息
     */
    private OrganizationTagDto organizationTagDto;
}
