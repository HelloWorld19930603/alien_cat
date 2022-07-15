package com.aliencat.springboot.elasticsearch.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@ToString
@Document(indexName = "search4message")
public class Message {
    @Field(type= FieldType.Auto)
    private Long id;
    @Field(type= FieldType.Auto)
    private Long businessActionId;
    @Field(type= FieldType.Auto)
    private Long virtualHumanId;
    @Field(type= FieldType.Auto)
    private String contactAccount;
    @Field(type= FieldType.Auto)
    private Long messageType;
    @Field(type= FieldType.Auto)
    private String messageContent;
    @Field(type= FieldType.Auto)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date messageTime;
    @Field(type= FieldType.Auto)
    private String messageFileUrl;
    @Field(type= FieldType.Auto)
    private String senderAccount;
    @Field(type= FieldType.Auto)
    private String recipientAccount;
    @Field(type= FieldType.Auto)
    private Integer isGroup;

}
