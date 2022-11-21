package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

import java.util.List;

/**
 * 组织信息
 */
@Data
public class OrgVo {
    /**
     * 组织信息
     */
    OrganizationTagDto organizationTagDto;

    /**
     * 组织对应的社交媒体账号信息
     */
    List<OrgToAccountEdgeDto> orgToAccountEdgeDtoList;

    /**
     * 组织和其它组织(学习,公司,组织)的关系
     */
    List<OrgToOrgEdgeDto> orgToOrgEdgeDtoList;

    /**
     * 组织对应的地址信息信息
     */
    List<OrgToAddressEdgeDto> orgToAddressEdgeDtoList;
}
