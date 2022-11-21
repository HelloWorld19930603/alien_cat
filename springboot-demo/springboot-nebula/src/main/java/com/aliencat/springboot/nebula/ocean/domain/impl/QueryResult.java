
package com.aliencat.springboot.nebula.ocean.domain.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliencat.springboot.nebula.ocean.dto.AccountTag;
import com.aliencat.springboot.nebula.ocean.dto.SocialAccountPostEdge;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Anyzm
 * @version 1.0.0
 * @Description QueryResult is used for
 * @Date 2020/3/27 - 10:13
 */
@ToString
@Slf4j
public class QueryResult implements Iterable<QueryResult.Row>, Cloneable, Serializable {

    @Getter
    private List<Row> data = Collections.emptyList();

    public QueryResult() {
    }

    public QueryResult(List<Row> data) {
        if (!CollectionUtils.isEmpty(data)) {
            this.data = data;
        }
    }

    @Override
    public QueryResult clone() {
        List<Row> newList = Lists.newArrayListWithExpectedSize(data.size());
        for (Row datum : data) {
            Row clone = datum.clone();
            newList.add(clone);
        }
        return new QueryResult(newList);
    }

    /**
     * 将查询结果合并
     *
     * @param queryResult
     * @return
     */
    public QueryResult mergeQueryResult(QueryResult queryResult) {
        if (queryResult == null || queryResult.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            this.data = queryResult.getData();
        } else {
            this.data.addAll(queryResult.getData());
        }
        return this;
    }

    public <T> Set<T> getEntities(Class<T> clazz) {
        long a = System.currentTimeMillis();
        List<T> list = new ArrayList<>();
        Set<T> set=new HashSet<>();
//        for (Row row : this.data) {
//            list.add(row.getEntity(clazz));
//        }
        this.data.stream().forEach(row -> set.add(row.getEntity(clazz)));
        long e = System.currentTimeMillis();
        log.info(" getEntities tt=" + (e - a));
        return set;
    }

    public int size() {
        return this.data.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean isNotEmpty() {
        return this.size() != 0;
    }

    @Override
    public Iterator<Row> iterator() {
        return this.data.iterator();
    }

    public Stream<Row> stream() {
        Iterable<Row> iterable = this::iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }


    @Data
    public static class Row implements Iterable<Map.Entry<String, Object>>, Cloneable, Serializable {

        private Map<String, Object> detail = Collections.emptyMap();

        public Row() {
        }

        public Row(Map<String, Object> detail) {
            if (MapUtils.isNotEmpty(detail)) {
                this.detail = detail;
            }
        }

        @Override
        public Row clone() {
            Map<String, Object> newMap = Maps.newHashMapWithExpectedSize(detail.size());
            newMap.putAll(detail);
            return new Row(newMap);
        }

        public int size() {
            return this.detail.size();
        }

        public Row setProp(String key, Object value) {
            this.detail.put(key, value);
            return this;
        }

        public Map<String, Object> getRowData() {
            return this.detail;
        }


        public Object get(String key) {
            return MapUtils.isEmpty(this.detail) ? null : this.detail.get(key);
        }


        public Date getDate(String key) {
            Long value = this.getLong(key);
            return new Timestamp(value * 1000);
        }

        public String getString(String columnLabel) {
            return getString(columnLabel, null);
        }

        public String getString(String columnLabel, String defaultValue) {
            return (String) this.detail.getOrDefault(columnLabel, defaultValue);
        }

        public Boolean getBoolean(String columnLabel) {
            return getBoolean(columnLabel, null);
        }

        public Boolean getBoolean(String columnLabel, Boolean defaultValue) {
            return (boolean) this.detail.getOrDefault(columnLabel, defaultValue);
        }

        public Short getShort(String columnLabel) {
            return getShort(columnLabel, null);
        }

        public Short getShort(String columnLabel, Short defaultValue) {
            return (short) this.detail.getOrDefault(columnLabel, defaultValue);
        }

        public Integer getInt(String columnLabel) {
            return getInt(columnLabel, null);
        }

        public Integer getInt(String columnLabel, Integer defaultValue) {
            Object obj = this.detail.get(columnLabel);
            if (obj instanceof Long) {
                long l = (Long) obj;
                if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Bad value for type int: " + l);
                }
                return (int) l;
            } else {
                return (int) this.detail.getOrDefault(columnLabel, defaultValue);
            }
        }

        public Long getLong(String columnLabel) {
            return getLong(columnLabel, null);
        }

        public Long getLong(String columnLabel, Long defaultValue) {
            return (long) this.detail.getOrDefault(columnLabel, defaultValue);
        }

        public Float getFloat(String columnLabel) {
            return getFloat(columnLabel, null);
        }

        public Float getFloat(String columnLabel, Float defaultValue) {
            return (float) this.detail.getOrDefault(columnLabel, defaultValue);
        }

        public Double getDouble(String columnLabel) {
            return getDouble(columnLabel, null);
        }

        public Double getDouble(String columnLabel, Double defaultValue) {
            Object value = this.detail.get(columnLabel);
            if (value instanceof Long) {
                long lValue = (Long) value;
                return (double) lValue;
            } else if (value instanceof Integer) {
                Integer lValue = (Integer) value;
                return (double) lValue;
            } else {
                return (double) this.detail.getOrDefault(columnLabel, 0.0);
            }
        }

        private static <T> T copyMapToBean(Map<String, ?> map, Class<T> clazz) {
//            long s1 = System.currentTimeMillis();
            String json = JSONObject.toJSONString(map);
//            long aa = System.currentTimeMillis();
//            log.info(" copyMapToBean time =" + (aa - s1));
//           T S= JSONObject.parseObject(json, clazz);
            return JSONObject.parseObject(json, clazz);
        }

        public static void main(String[] args) {
            AccountTag a= JSONObject.parseObject("{\"collectTime\":1642083282,\"fanOtherCount\":13083,\"isProtect\":false,\"joinTime\":1493460003,\"articleCount\":58407,\"likeCount\":6609,\"updateTime\":1650855897,\"likedCount\":0,\"describeInfo\":\"欢迎通过PayPal支持我httpstcox3XOzH62kF廖德明毕业于中国四川美术学院追求完美性以我1000多幅大型油画作品做成艺术家品牌上市创建廖德明古典主义艺术博物馆和古典艺术学院寻找经纪人欢迎投资赞助热爱美国定居纽约市新保守价值支持人类主义\",\"userName\":\"LiaoDeming\",\"userId\":\"858259578181500928\",\"isVerify\":false,\"url\":\"https://twitter.com/LiaoDeming\",\"headPhoto\":\"5163/1305327557026422786_hWAnsgsQ_400x400.jpg\",\"fanCount\":11915,\"descCn\":\"欢迎通过PayPal支持我httpstcox3XOzH62kF廖德明毕业于中国四川美术学院追求完美性以我1000多幅大型油画作品做成艺术家品牌上市创建廖德明古典主义艺术博物馆和古典艺术学院寻找经纪人欢迎投资赞助热爱美国定居纽约市新保守价值支持人类主义\",\"appType\":1,\"nickname\":\"中国意识翻译\",\"id\":\"979c1099c024fe288a796b2d3df145fd\",\"email\":\"null\"}", AccountTag.class);
            SocialAccountPostEdge S= JSONObject.parseObject("{\"state\":false}", SocialAccountPostEdge.class);
        }
        public <T> T getEntity(Class<T> clazz) {
            return copyMapToBean(this.detail, clazz);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Row row = (Row) o;
            return Objects.equals(this.detail, row.detail);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.detail);
        }

        @Override
        public Iterator<Map.Entry<String, Object>> iterator() {
            return this.detail.entrySet().iterator();
        }
    }

}
