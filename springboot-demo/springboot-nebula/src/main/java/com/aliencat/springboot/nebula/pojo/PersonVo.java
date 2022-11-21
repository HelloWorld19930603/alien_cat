package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

import java.util.List;

/**
 * 图数据库人员信息
 */
@Data
public class PersonVo {
    /**
     * 当前人员信息
     */
    PersonInfoTagDto personInfoTagDto;

    /**
     * 人员对应的社交媒体账号信息
     */
    List<PersonToAccountEdgeDto> personToAccountEdgeDtoList;

    /**
     * 人员对应的组织(学习,公司,组织)信息
     */
    List<PersonToOrgEdgeDto> personToOrgEdgeDtoList;

    /**
     * 人员对应的手机号码信息
     */
    List<PersonToPhoneNumEdgeDto> personToPhoneNumEdgeDtoList;

    /**
     * 人员对应的邮箱信息
     */
    List<PersonToEmailEdgeDto> personToEmailEdgeDtoList;

    /**
     * 人员对应的地址信息信息
     */
    List<PersonToAddressEdgeDto> personToAddressEdgeDtoList;

    /**
     * 人员与人员之间的关系
     */
    List<PersonToPersonEdgeDto> personToPersonEdgeDtoList;
}
