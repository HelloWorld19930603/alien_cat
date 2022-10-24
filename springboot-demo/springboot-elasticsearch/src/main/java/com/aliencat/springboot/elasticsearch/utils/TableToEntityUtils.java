package com.aliencat.springboot.elasticsearch.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengcheng
 * @Date 2022-09-07
 **/
public class TableToEntityUtils {


    static final String USER = "root";
    static final String PASS = "yagoo@110";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://192.168.2.5:3306/bigdata?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowMultiQueries=true&rewriteBatchedStatements=true";
    static final String DB_URL = "jdbc:mysql://tidb-pd1.aim:4000/bigdata?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true";

    public static void main(String[] args) throws Exception {
        getMessageMaps("2018-03-01 00:00:00","2021-07-02 00:00:00", null);
    }

    private static Connection conn = null;
    private static Statement stmt = null;
    private static Statement getStatement() throws SQLException {

        if(stmt != null && !stmt.isClosed()){
            return stmt;
        }
        // 注册 JDBC 驱动
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return stmt;
    }


    public static void close(){
        try{
            stmt.close();
            conn.close();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null){
                    stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            stmt = null;
            conn = null;
        }
    }

    public static List<Map<String,Object>> getMessageMaps(String start, String end, String contact) {
        try{
            getStatement();
            String sql;
            if(!StringUtils.isEmpty(contact)){
                sql= "SELECT * FROM im_messages  WHERE contact_account = "+contact +" or recipient_account = "+contact
                        +" or sender_account = "+contact;
            }else {
                sql= "SELECT * FROM im_messages  WHERE message_time >= '"+start+"' AND message_time < '"+end+"'";
            }
            return getMessageMaps(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Map<String,Object>> getMessageMapsByIds(List<Long> ids) {
        try{
            getStatement();
            String sql = "SELECT * FROM im_messages  WHERE id in ";
            return getMessageMaps(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Map<String,Object>> getMessageMaps(String sql) throws SQLException {
        List<Map<String,Object>> tableFieldList=new ArrayList<>(100000);
        ResultSet rs = stmt.executeQuery(sql);
        int i = 0;
        while (rs.next()) {
            Map<String,Object> fieldMap = new HashMap(32);
            fieldMap.put("business_action_id",rs.getInt("business_action_id"));
            fieldMap.put("is_group",rs.getInt("is_group"));
            fieldMap.put("id",rs.getLong("id"));
            fieldMap.put("message_type",rs.getInt("message_type"));
            fieldMap.put("contact_account",rs.getString("contact_account"));
            fieldMap.put("sender_account",rs.getString("sender_account"));
            fieldMap.put("recipient_account",rs.getString("recipient_account"));
            fieldMap.put("message_content",rs.getString("message_content"));
            fieldMap.put("message_time",rs.getTimestamp("message_time").getTime() / 1000);
            fieldMap.put("gmt_create",rs.getTimestamp("gmt_create").getTime() / 1000);
            fieldMap.put("message_file_url",rs.getString("message_file_url"));
            String message_misc_long = rs.getString("message_misc_long");
            if(!StringUtils.isEmpty(message_misc_long)){
                try {
                    JSONObject jsonObject = JSONObject.parseObject(message_misc_long);
                    fieldMap.put("m_l_charCount", jsonObject.get("charCount"));
                    fieldMap.put("m_l_senderType", jsonObject.get("informationType"));
                    fieldMap.put("m_l_informationType", jsonObject.get("senderType"));
                }catch (Exception e){
                    System.out.println("json转换异常");
                }
            }
               /* if(++i == 10000){
                    System.out.println(fieldMap.get("id") + " - " + fieldMap.get("contact_account") + " - " + fieldMap.get("sender_account")
                            + " - " + fieldMap.get("message_time"));
                }*/
            tableFieldList.add(fieldMap);
        }
        rs.close();
        return tableFieldList;
    }
}
