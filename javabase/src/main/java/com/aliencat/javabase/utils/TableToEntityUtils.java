package com.aliencat.javabase.utils;

import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    static final String PASS = "YG@root#safe";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.168.2.5:3306/regional_trend_v28?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowMultiQueries=true&rewriteBatchedStatements=true";

    public static void main(String[] args) throws Exception {
        createEntity("regional_trend_v28", "operation_log","com.yg.regional.trend.db.entity",false);
    }

    /**
     *
     * @param dataBaseName 数据库名
     * @param tableName 表名
     * @param packageName 包名
     * @param isAddEntitySuffix 类名是否添加Entity后缀 true-添加 false-不添加
     */
    public static void createEntity(String dataBaseName, String tableName, String packageName, boolean isAddEntitySuffix) throws Exception{
        String className=tableName;
        if(tableName.substring(0,2).equals("t_")){
            StringBuilder stringBuilder = new StringBuilder(tableName);
            stringBuilder.replace(0, 2, "");
            String initialsUpperCase = stringBuilder.substring(0, 1).toUpperCase();
            className=initialsUpperCase+stringBuilder.substring(1);
        }
        className=removeUnderline(className)+(isAddEntitySuffix?"Entity":"");

        StringBuffer classBuffer=new StringBuffer();
        classBuffer.append("import java.util.Date;\r\n");
        classBuffer.append("import java.time.LocalDateTime;\r\n");
        classBuffer.append("import com.alibaba.fastjson.JSONObject;\r\n");
        classBuffer.append("import java.lang.*;\r\n");
        classBuffer.append("import java.math.*;\r\n");
        classBuffer.append("import java.sql.*;\r\n");
        classBuffer.append("import lombok.Data;\r\n\r\n\r\n");
        classBuffer.append("@Data\r\n");
        classBuffer.append("public class " + className + " {\r\n\r\n");
        List<Map> filedMaps = getFiledMaps(dataBaseName, tableName);
        processAllAttrs(classBuffer,filedMaps);
        classBuffer.append("}\r\n");
        markerBean(className,classBuffer.toString(),packageName);
    }

    /**
     * 创建实体类文件
     * @param className 类名(不包含.java文件名后缀) 根据表名首字母大写并去掉开头t_和所有下划线-驼峰命名
     * @param content 添加的内容(字段注释等)
     * @param packageName 包名(com.xxx.xxx.xxx)
     */
    public static void markerBean(String className, String content, String packageName) throws Exception {
//      这里不使用System.getProperty("user.dir")了。user.dir是根据运行时环境来的
        File f2 = new File(TableToEntityUtils.class.getResource("/").getPath());
        String homePath=f2.getCanonicalPath().replace("\\target\\classes", "");
        String folder = homePath + "/src/main/java/" + packageName.replace(".","/") + "/";
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = folder + className + ".java";
        try {
            File newjava = new File(fileName);
            FileWriter fw = new FileWriter(newjava);
            fw.write("package\t" + packageName + ";\r\n");
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去下划线
     * @param str
     * @return
     */
    public static String removeUnderline(String str){
        StringBuilder columnNameBuilder=new StringBuilder(str);
        if(!str.contains("_")){
            return columnNameBuilder.toString();
        }else {
            int i = columnNameBuilder.indexOf("_");
            columnNameBuilder.replace(i,i+1, "").replace(i,i+1,columnNameBuilder.substring(i,i+1).toUpperCase());
            return removeUnderline(columnNameBuilder.toString());
        }
    }

    /**
     * 获取表字段信息(列名、类型、注释等)
     * @param dataBaseName
     * @param tableName
     * @return
     */
    private static List<Map> getFiledMaps(String dataBaseName, String tableName) {
        Connection conn = null;
        Statement stmt = null;
        List<Map> tableFieldList=new ArrayList<>();
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql= "SELECT * FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA ='"+dataBaseName+"' AND TABLE_NAME= '"+tableName+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String,String> fieldMap = new HashMap();
                String column_name = rs.getString("COLUMN_NAME");
                setFieldName(column_name.toLowerCase(),fieldMap);
                String data_type = rs.getString("DATA_TYPE");
                setFieldType(data_type.toUpperCase(),fieldMap);
                fieldMap.put("fieldComment",rs.getString("COLUMN_COMMENT"));
                tableFieldList.add(fieldMap);
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
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
        }
        return tableFieldList;
    }

    /**
     * 解析输出属性
     *
     * @return
     */
    private static void processAllAttrs(StringBuffer sb, List<Map> filedMaps) {
        for (int i = 0; i < filedMaps.size(); i++) {
            Map map = filedMaps.get(i);
            String fieldType = MapUtils.getString(map, "fieldType");
            String fieldName = MapUtils.getString(map, "fieldName");
            String fieldComment = MapUtils.getString(map, "fieldComment");
            if(StringUtils.isNotBlank(fieldComment)){
                sb.append("\t/**\r\n").append("\t* ").append(fieldComment).append("\n").append("\t*/\r\n");
            }
            sb.append("\tprivate " + fieldType + " " + fieldName + ";\r\n\r\n");
        }
    }

    private static void setFieldName(String columnName, Map fieldMap) {
        fieldMap.put("fieldName",removeUnderline(columnName));
    }

    private static void setFieldType(String columnType, Map fieldMap){
        String fieldType="String";
        if(columnType.equals("INT")||columnType.equals("INTEGER")){
            fieldType="Integer";
        }else if(columnType.equals("BIGINT")){
            fieldType="Long";
        }else if(columnType.equals("DATETIME")){
            fieldType="Date";
        }else if(columnType.equals("TEXT")||columnType.equals("VARCHAR")||columnType.equals("TINYTEXT")||columnType.equals("LONGTEXT")){
            fieldType="String";
        }else if(columnType.equals("DOUBLE")){
            fieldType="Double";
        }else if(columnType.equals("BIT")){
            fieldType="Boolean";
        }else if(columnType.equals("FLOAT")){
            fieldType="Float";
        }else if(columnType.equals("DECIMAL")){
            fieldType="BigDecimal";
        }else if(columnType.equals("DATE")){
            fieldType="Date";
        }else if(columnType.equals("TIMESTAMP")){
            fieldType="LocalDateTime";
        }else if(columnType.equals("CHAR")){
            fieldType="Char";
        }else if(columnType.equals("JSON")){//mysql5.7版本才开始有的
            fieldType="JSONObject";
        }
        fieldMap.put("fieldType",fieldType);
    }
}
