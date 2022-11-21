package com.aliencat.springboot.nebula.ocean.dto;


import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.dto.linkedin.BaseTagOrEdge;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 社交帐号的点表
 */
@GraphVertex(value = "accountTag", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class AccountTag extends BaseTagOrEdge {
    /**
     * 帐号生成的uuid.
     */
    @GraphProperty(value = "id", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_VERTEX_ID)
    private String id;

    /**
     * app类型，int32, 1-twitter 2-facebook 3-youtube
     */
    @GraphProperty(value = "appType", required = true, dataType = GraphDataTypeEnum.INT)
    private Integer appType;

    /**
     * 发文总量
     */
    @GraphProperty(value = "articleCount", required = true, dataType = GraphDataTypeEnum.INT)
    private Long articleCount;

    /**
     * 采集时间戳(秒)
     */
    @GraphProperty(value = "collectTime", required = true, dataType = GraphDataTypeEnum.INT)
    private Long collectTime;

    /**
     * 用户简介
     */
    @GraphProperty(value = "describeInfo", required = true, dataType = GraphDataTypeEnum.STRING)
    private String describeInfo;

    /**
     * 用户简介(译文)
     */
    @GraphProperty(value = "descCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String descCn;

    /**
     * 邮箱
     */
    @GraphProperty(value = "email", required = true, dataType = GraphDataTypeEnum.STRING)
    private String email;

    /**
     * 粉丝总量
     */
    @GraphProperty(value = "fanCount", required = true, dataType = GraphDataTypeEnum.INT)
    private Integer fanCount;

    /**
     * 粉他人总量
     */
    @GraphProperty(value = "fanOtherCount", required = true, dataType = GraphDataTypeEnum.INT)
    private Integer fanOtherCount;
//
//    /**
//     * 生成的唯一ID
//     */
//    @GraphProperty(value = "userGenUuid", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String userGenUuid;

    /**
     * 头像文件路径
     */
    @GraphProperty(value = "headPhoto", required = true, dataType = GraphDataTypeEnum.STRING)
    private String headPhoto;

    /**
     * 是否保护
     */
    @GraphProperty(value = "isProtect", required = true, dataType = GraphDataTypeEnum.BOOL)
    private Boolean isProtect;

    /**
     * 是否验证
     */
    @GraphProperty(value = "isVerify", required = true, dataType = GraphDataTypeEnum.BOOL)
    private Boolean isVerify;

    /**
     * 加入时间戳(秒)
     */
    @GraphProperty(value = "joinTime", required = true, dataType = GraphDataTypeEnum.INT)
    private Long joinTime;

    /**
     * 账号赞他人总数
     */
    @GraphProperty(value = "likeCount", required = true, dataType = GraphDataTypeEnum.INT)
    private Integer likeCount;

    /**
     * 账号被赞总数
     */
    @GraphProperty(value = "likedCount", required = true, dataType = GraphDataTypeEnum.INT)
    private Integer likedCount;
//
//    /**
//     * 所在位置
//     */
//    @GraphProperty(value = "location", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String location;

    /**
     * 昵称
     */
    @GraphProperty(value = "nickname", required = true, dataType = GraphDataTypeEnum.STRING)
    private String nickname;

    /**
     * 更新时间戳(秒)
     */
    @GraphProperty(value = "updateTime", required = true, dataType = GraphDataTypeEnum.INT)
    private Long updateTime;

    /**
     * 用户网络地址
     */
    @GraphProperty(value = "url", required = true, dataType = GraphDataTypeEnum.STRING)
    private String url;

    /**
     * 用户ID
     */
    @GraphProperty(value = "userId", required = true, dataType = GraphDataTypeEnum.STRING)
    private String userId;

    /**
     * 用户名
     */
    @GraphProperty(value = "userName", required = true, dataType = GraphDataTypeEnum.STRING)
    private String userName;
}