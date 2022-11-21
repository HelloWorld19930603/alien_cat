package com.aliencat.springboot.nebula.ocean.dto.linkedin;


import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 人员与组织之间的关系
 */
@Data
@GraphEdge(value = "PersonToOrgEdge", srcVertex = PersonInfoTag.class, dstVertex = OrganizationTag.class)
public class PersonToOrgEdge extends BaseTagOrEdge{
    @GraphProperty(value = "fromId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_SRC_ID)
    private String fromId;
    @GraphProperty(value = "toId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_DST_ID)
    private String toId;

//    /**
//     * 遍历
//     */
//    @GraphProperty(value = "ergodic", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String ergodic;
//
//    /**
//     * 来源
//     */
//    @GraphProperty(value = "source", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String source;

    /**
     * (1:组织 2:学校 3:企业)
     */
    @GraphProperty(value = "organizationType", required = true, dataType = GraphDataTypeEnum.STRING)
    private String organizationType;

    /**
     * 学位
     */
    @GraphProperty(value = "degrees", required = true, dataType = GraphDataTypeEnum.STRING)
    private String degrees;

    /**
     * 专业
     */
    @GraphProperty(value = "majors", required = true, dataType = GraphDataTypeEnum.STRING)
    private String majors;

    /**
     * 职位名称,头衔
     */
    @GraphProperty(value = "title", required = true, dataType = GraphDataTypeEnum.STRING)
    private String title;

    /**
     * 总结
     */
    @GraphProperty(value = "summary", required = true, dataType = GraphDataTypeEnum.STRING)
    private String summary;

    /**
     * 学位(中文翻译)
     */
    @GraphProperty(value = "degreesCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String degreesCn;

    /**
     * 专业(中文翻译)
     */
    @GraphProperty(value = "majorsCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String majorsCn;

    /**
     * 职位名称,头衔(中文翻译)
     */
    @GraphProperty(value = "titleCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String titleCn;

    /**
     * 总结(中文翻译)
     */
    @GraphProperty(value = "summaryCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String summaryCn;

    @GraphProperty(value = "startDate", required = true, dataType = GraphDataTypeEnum.STRING)
    private String startDate;

    @GraphProperty(value = "endDate", required = true, dataType = GraphDataTypeEnum.STRING)
    private String endDate;
}
