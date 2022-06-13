package com.aliencat.springboot.mongodb.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 实体层
 */
@Document(collection="user")
public class User {

    @Id // 指定ID
    private String id;

    @Field("name") // 指定域名，覆盖默认
    private String name;

    @Field("password") // 指定域名，覆盖默认
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
