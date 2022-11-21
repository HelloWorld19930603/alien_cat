package com.aliencat.springboot.nebula.ocean.dto;

import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 社交帐号关系的基础映射表
 */
@Data
@GraphEdge(value = "SocialAccountRelationInfo", srcVertex = AccountTag.class, dstVertex = AccountTag.class)
public class SocialAccountRelationInfo {
    @GraphProperty(value = "fromId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_SRC_ID)
    private String fromId;
    @GraphProperty(value = "toId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_DST_ID)
    private String toId;

    /**
     * 边的数量ID,方便遍历与查询
     */
    @GraphProperty(value = "numId", required = true, dataType = GraphDataTypeEnum.INT)
    private int numId;

    /**
     * 关注.(0:没有关注  1:有关注)
     */
    @GraphProperty(value = "follow", required = true, dataType = GraphDataTypeEnum.INT)
    private int follow;

    /**
     * 点赞数量.
     */
    @GraphProperty(value = "like", required = true, dataType = GraphDataTypeEnum.INT)
    private int like;

    /**
     * 转发数量.
     */
    @GraphProperty(value = "retransmission", required = true, dataType = GraphDataTypeEnum.INT)
    private int retransmission;

    /**
     * 评论数量.
     */
    @GraphProperty(value = "reply", required = true, dataType = GraphDataTypeEnum.INT)
    private int reply;

    /**
     * 引用数量.
     */
    @GraphProperty(value = "quote", required = true, dataType = GraphDataTypeEnum.INT)
    private int quote;

    /**
     * 账号之间类型(0: 普通账号对普通账号 1: 普通账号对认证账号  2 : 认证账号对普通账号   3: 认证账号对认证账号).
     */
    @GraphProperty(value = "typesBetweenAccounts", required = true, dataType = GraphDataTypeEnum.INT)
    private int typesBetweenAccounts;

    /**
     * 账号操作(关注,点赞,转发,评论,引用)计算出来的亲密度.
     */
    @GraphProperty(value = "closenessByOperation", required = true, dataType = GraphDataTypeEnum.DOUBLE)
    private double closenessByOperation;

    /**
     * 账号之间关联方式(1 : 单方面  2: 互相关注   3 : 无互相关注，有互动  4: 互相关注，并有互动)
     */
    @GraphProperty(value = "associationMode", required = true, dataType = GraphDataTypeEnum.INT)
    private int associationMode;

    /**
     * 系统计算的亲密度.
     */
    @GraphProperty(value = "closenessBySystem", required = true, dataType = GraphDataTypeEnum.DOUBLE)
    private double closenessBySystem;

    /**
     * 用户标注的亲密度值.
     */
    @GraphProperty(value = "closenessValue", required = true, dataType = GraphDataTypeEnum.STRING)
    private String closenessValue;
    /**
     * 用户标注的亲密度的备注.
     */
    @GraphProperty(value = "closenessRemark", required = true, dataType = GraphDataTypeEnum.STRING)
    private String closenessRemark;
    /**
     * 标注亲密度的工作组id
     */
    @GraphProperty(value = "userId", required = true, dataType = GraphDataTypeEnum.STRING)
    private String userId;

    /**
     * 总亲密度.
     */
    @GraphProperty(value = "totalCloseness", required = true, dataType = GraphDataTypeEnum.DOUBLE)
    private double totalCloseness;


    public SocialAccountRelationInfo() {
    }

    public SocialAccountRelationInfo(String fromId, String toId, int follow, int like, int retransmission, int reply, int quote, double closenessBySystem) {
        this.fromId = fromId;
        this.toId = toId;
        this.follow = follow;
        this.like = like;
        this.retransmission = retransmission;
        this.reply = reply;
        this.quote = quote;
        this.closenessBySystem = closenessBySystem;
    }
//    /**
//     * 用户标注的亲密度.
//     */
//    private List<ClosenessByUser> closenessListByUser;
//    /**
//     * 从起点到终点是否有“关点转评引”.
//     */
//    private ClosenessParam directionOutbound;
//
//    /**
//     * 用户标注的亲密度.
//     */
//    @Data
//    public static class ClosenessByUser {
//        /**
//         * 用户标注的亲密度值.
//         */
//        private double closenessValue;
//        /**
//         * 用户标注的亲密度的备注.
//         */
//        private String closenessRemark;
//        /**
//         * 标注亲密度的工作组id
//         */
//        private String workGroupId;
//    }
//
//    /**
//     * 计算亲密度的参数.
//     */
//    @Data
//    public static class ClosenessParam {
//        /**
//         * 是否关注.
//         */
//        private boolean follow;
//        /**
//         * 是否点赞.
//         */
//        private boolean like;
//        /**
//         * 是否转发.
//         */
//        private boolean retransmission;
//        /**
//         * 是否评论.
//         */
//        private boolean reply;
//        /**
//         * 是否引用.
//         */
//        private boolean quote;
//    }
}
