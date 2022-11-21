package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 人员与组织之间的关系
 */
@Data
public class PersonToOrgEdgeDto {
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
     * (1:组织 2:学校 3:企业)
     */
    private String organizationType;

    /**
     * 学位
     */
    private String degrees;

    /**
     * 专业
     */
    private String majors;

    /**
     * 职位名称,头衔
     */
    private String title;

    /**
     * 总结
     */
    private String summary;

    /**
     * 学位(中文翻译)
     */
    private String degreesCn;

    /**
     * 专业(中文翻译)
     */
    private String majorsCn;

    /**
     * 职位名称,头衔(中文翻译)
     */
    private String titleCn;

    /**
     * 总结(中文翻译)
     */
    private String summaryCn;

    /**
     * 组织的点信息
     */
    private OrganizationTagDto organizationTagDto;
}
